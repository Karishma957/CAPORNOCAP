import { Injectable, NgZone } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { DeveloperConsoleState } from "../model/DeveloperConsoleState";
import { environment } from "../../environments";

@Injectable({
    providedIn: 'root'
})
export class DevConsoleService {
    private _data$ = new BehaviorSubject<DeveloperConsoleState | null>(null);
    data$ = this._data$.asObservable();
    private es?: EventSource;
    private baseUrl = environment.production ? '/api' : environment.backendBaseUrl;

    constructor(private zone: NgZone) { }

    start() {
        console.log("[DevConsoleService] Starting service…");

        const snapshotUrl = this.baseUrl + `/dev-console/snapshot`;
        console.log("[DevConsoleService] Fetching snapshot from:", snapshotUrl);

        fetch(snapshotUrl)
            .then(response => {
                console.log("[DevConsoleService] Snapshot response status:", response.status);
                return response.json();
            })
            .then((data: DeveloperConsoleState) => {
                console.log("[DevConsoleService] Snapshot data:", data);
                this._data$.next(data);
            })
            .catch(err => console.error("[DevConsoleService] Snapshot error:", err));

        const streamUrl = this.baseUrl + `/dev-console/stream`;
        console.log("[DevConsoleService] Opening EventSource to:", streamUrl);

        this.es = new EventSource(streamUrl);

        this.es.onopen = () => {
            console.log("[DevConsoleService] SSE connection opened:", streamUrl);
        };

        this.es.onmessage = (event) => {
            console.log("[DevConsoleService] SSE raw event:", event.data);
            this.zone.run(() => {
                try {
                    const parsedData: DeveloperConsoleState = JSON.parse(event.data);
                    console.log("[DevConsoleService] Parsed SSE data:", parsedData);
                    this._data$.next(parsedData);
                } catch (e) {
                    console.error("[DevConsoleService] Error parsing event data:", e, "Raw:", event.data);
                }
            });
        };

        this.es.onerror = (error) => {
            console.error("[DevConsoleService] SSE error:", error);
        };
    }

    stop() {
        this.es?.close();
    }
}
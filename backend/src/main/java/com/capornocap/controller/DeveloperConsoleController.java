package com.capornocap.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.capornocap.dto.DeveloperConsoleState;
import com.capornocap.service.DeveloperConsoleService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/dev-console")
@RequiredArgsConstructor
public class DeveloperConsoleController {
    private final DeveloperConsoleService developerConsoleService;

    @GetMapping("/snapshot")
    public DeveloperConsoleState snapshot() {
        return developerConsoleService.snapshot();
    }

    @GetMapping("/stream")
    public SseEmitter stream() {
        return developerConsoleService.registerEmitter();
    }
}

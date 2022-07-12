package com.woowacourse.levellog.presentation;

import com.woowacourse.levellog.application.LevellogService;
import com.woowacourse.levellog.dto.LevellogCreateRequest;
import com.woowacourse.levellog.dto.LevellogResponse;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/levellogs")
@RequiredArgsConstructor
public class LevellogController {

    private final LevellogService levellogService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid final LevellogCreateRequest request) {
        final Long id = levellogService.save(request);
        return ResponseEntity.created(URI.create("/api/levellogs/" + id)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LevellogResponse> find(@PathVariable final Long id) {
        final LevellogResponse response = levellogService.find(id);
        return ResponseEntity.ok(response);
    }
}

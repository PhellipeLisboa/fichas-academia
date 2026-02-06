package com.phellipe.workoutplanner.backend.controller;

import com.phellipe.workoutplanner.backend.dto.blockItem.BlockItemResponse;
import com.phellipe.workoutplanner.backend.dto.blockItem.CreateBlockItemRequest;
import com.phellipe.workoutplanner.backend.dto.blockItem.ReorderBlockItemsRequest;
import com.phellipe.workoutplanner.backend.dto.blockItem.UpdateBlockItemRequest;
import com.phellipe.workoutplanner.backend.service.BlockItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/block-items")
public class BlockItemController {

    private final BlockItemService blockItemService;

    @PostMapping
    public ResponseEntity<BlockItemResponse> createBlockItem(@Valid @RequestBody CreateBlockItemRequest request) {
        BlockItemResponse response = blockItemService.createBlockItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlockItemResponse> updateBlockItem(@PathVariable Long id, @Valid @RequestBody UpdateBlockItemRequest request) {
        BlockItemResponse response = blockItemService.updateBlockItem(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/block/{blockId}/reorder")
    public ResponseEntity<List<BlockItemResponse>> reorderItems(@PathVariable Long blockId, @Valid @RequestBody ReorderBlockItemsRequest request) {
        List<BlockItemResponse> response = blockItemService.reorderItems(blockId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlockItem(@PathVariable Long id) {
        blockItemService.deleteBlockItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlockItemResponse> getBlockItemById(@PathVariable Long id) {
        BlockItemResponse response = blockItemService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/block/{blockId}")
    public ResponseEntity<List<BlockItemResponse>> getItemsByBlock(@PathVariable Long blockId) {
        List<BlockItemResponse> response = blockItemService.findAllByBlockId(blockId);
        return ResponseEntity.ok(response);
    }

}

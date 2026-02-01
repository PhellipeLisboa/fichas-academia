package com.phellipe.workoutplanner.backend.dto.blockItem;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ReorderBlockItemsRequest {

    @NotEmpty(message = "Item IDs list cannot be empty")
    private List<Long> itemIds;
}

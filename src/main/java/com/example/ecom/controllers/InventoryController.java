package com.example.ecom.controllers;

import com.example.ecom.dtos.*;
import com.example.ecom.exceptions.ProductNotFoundException;
import com.example.ecom.exceptions.UnAuthorizedAccessException;
import com.example.ecom.exceptions.UserNotFoundException;
import com.example.ecom.services.InventoryService;

public class InventoryController {


    private InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public CreateOrUpdateResponseDto createOrUpdateInventory(CreateOrUpdateRequestDto requestDto){
        CreateOrUpdateResponseDto responseDto = new CreateOrUpdateResponseDto();
        try {
            responseDto.setInventory(inventoryService.createOrUpdateInventory(requestDto.getUserId(), requestDto.getProductId(), requestDto.getQuantity()));
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (UserNotFoundException | ProductNotFoundException | UnAuthorizedAccessException e) {
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }

    public DeleteInventoryResponseDto deleteInventory(DeleteInventoryRequestDto requestDto){
        DeleteInventoryResponseDto responseDto = new DeleteInventoryResponseDto();
        try {
            inventoryService.deleteInventory(requestDto.getUserId(), requestDto.getProductId());
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (UserNotFoundException | UnAuthorizedAccessException e) {
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }


}

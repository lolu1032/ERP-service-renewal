package com.erp.mes.service;

import com.erp.mes.dto.ShipmentDTO;
import com.erp.mes.dto.StockDTO;
import com.erp.mes.mapper.ShipmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShipmentService {
    private final ShipmentMapper shipmentMapper;
    private final StockService stockService;

    @Transactional
    public int createShipment(ShipmentDTO shipmentDTO) {
        StockDTO stock = stockService.getStockDetails(shipmentDTO.getStkId());
        if (stock == null) {
            return -1;  // 재고를 찾을 수 없는 경우
        }

        if (stock.getQty() < shipmentDTO.getReqQty()) {
            return -2;  // 재고 부족
        }

        // 재고가 충분한 경우
        shipmentDTO.setStatus("REQUESTED");
        shipmentDTO.setQty(shipmentDTO.getReqQty());
        int result = shipmentMapper.insertShipment(shipmentDTO);

        // 재고 차감
        shipmentMapper.updateStockAfterShipment(shipmentDTO.getStkId(), shipmentDTO.getReqQty());

        return result;
    }

    public List<ShipmentDTO> getShipmentList(Map<String, Object> params) {
        return shipmentMapper.selectShipmentList(params);
    }

    @Transactional
    public void completeShipment(int shipId, int qty) {
        ShipmentDTO shipment = shipmentMapper.selectShipmentById(shipId);
        if (shipment == null) {
            throw new RuntimeException("해당 출고 요청을 찾을 수 없습니다.");
        }
        if (!"REQUESTED".equals(shipment.getStatus())) {
            throw new RuntimeException("이미 처리된 출고 요청입니다.");
        }
        if (shipment.getAvailableQty() < qty) {
            throw new RuntimeException("재고가 부족합니다.");
        }
        int stockUpdateResult = shipmentMapper.updateStockAfterShipment(shipment.getStkId(), qty);
        if (stockUpdateResult <= 0) {
            throw new RuntimeException("재고 차감에 실패했습니다.");
        }
        int shipmentCompletionResult = shipmentMapper.updateShipmentStatusToCompleted(shipId, qty);
        if (shipmentCompletionResult <= 0) {
            throw new RuntimeException("출고 완료 상태로 업데이트 실패");
        }
    }

    public int cancelShipment(int shipId) {
        return shipmentMapper.cancelShipment(shipId);
    }

    @Transactional
    public int updateShipmentStatus(int shipId, String status) {
        ShipmentDTO shipment = shipmentMapper.selectShipmentById(shipId);
        if (shipment == null) {
            throw new RuntimeException("해당 출고 요청을 찾을 수 없습니다.");
        }

        if ("COMPLETED".equals(status)) {
            // 출고 완료 처리
            int availableStock = shipmentMapper.getAvailableStock(shipment.getStkId());
            if (availableStock < shipment.getReqQty()) {
                throw new RuntimeException("재고가 부족합니다. 현재 재고: " + availableStock);
            }
            shipmentMapper.updateStockAfterShipment(shipment.getStkId(), shipment.getReqQty());
        }

        return shipmentMapper.updateShipmentStatus(shipId, status);
    }

    public ShipmentDTO selectShipmentById(int shipId) {
        return shipmentMapper.selectShipmentById(shipId);
    }

    public List<ShipmentDTO> searchShipments(Map<String, Object> params) {
        return shipmentMapper.selectShipmentList(params);
    }

    // 새로 추가된 메서드
    public ShipmentDTO checkStockForShipment(int shipId) {
        ShipmentDTO shipment = shipmentMapper.selectShipmentById(shipId);
        if (shipment == null) {
            throw new RuntimeException("해당 출고 요청을 찾을 수 없습니다.");
        }
        int availableStock = shipmentMapper.getAvailableStock(shipment.getStkId());
        shipment.setAvailableQty(availableStock);
        return shipment;
    }

    @Transactional
    public void partialCompleteShipment(int shipId, int qty) {
        ShipmentDTO shipment = shipmentMapper.selectShipmentById(shipId);
        if (shipment == null) {
            throw new RuntimeException("해당 출고 요청을 찾을 수 없습니다.");
        }
        if (qty > shipment.getReqQty()) {
            throw new RuntimeException("출고 수량이 요청 수량을 초과합니다.");
        }
        int availableStock = shipmentMapper.getAvailableStock(shipment.getStkId());
        if (qty > availableStock) {
            throw new RuntimeException("재고가 부족합니다.");
        }
        shipmentMapper.updateShipmentQuantity(shipId, qty);
        shipmentMapper.updateStockAfterShipment(shipment.getStkId(), qty);
        String newStatus = qty == shipment.getReqQty() ? "COMPLETED" : "PARTIALLY_COMPLETED";
        shipmentMapper.updateShipmentStatus(shipId, newStatus);
    }

    @Transactional
    public void updateShipmentQuantity(int shipId, int newQty) {
        ShipmentDTO shipment = shipmentMapper.selectShipmentById(shipId);
        if (shipment == null) {
            throw new RuntimeException("해당 출고 요청을 찾을 수 없습니다.");
        }
        int availableStock = shipmentMapper.getAvailableStock(shipment.getStkId());
        if (newQty > availableStock) {
            throw new RuntimeException("재고가 부족합니다.");
        }
        shipmentMapper.updateShipmentQuantity(shipId, newQty);
    }


}
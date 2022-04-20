package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MyCart extends BaseModel{

    private UUID userId;
    private UUID productId;
    private boolean isSold;
    private double price;
    private int amount;
    private String productName;


    @Override
    public String toString() {
        return "\t|\tPRODUCT NAME: " + productName +
                "| PRICE: " + price +
                "\t|\tAMOUNT: " + amount + " |";
    }
}

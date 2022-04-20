package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product extends BaseModel {
    private double price;
    private int amount;
//    private UUID sellerUUID;
    private String sellerId;
    private UUID categoryId;
    private String productInfo;
    private String photoStatus;
    private String fileUrlPhoto;

    @Override
    public String toString() {
        return "name :   " + name +
                "\ncreatedDate :   " + createdDate +
                "\nupdatedDate :   " + updatedDate +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", isActive=" + isActive +
                ", price=" + price +
                ", amount=" + amount +
                ", sellerId='" + sellerId + '\'' +
                ", categoryId=" + categoryId +
                ", productInfo='" + productInfo + '\'' +
                ", photoStatus='" + photoStatus + '\'' +
                ", fileUrlPhoto='" + fileUrlPhoto;
    }
}
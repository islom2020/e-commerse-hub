package repository;

import model.Product;

import java.util.List;
import java.util.UUID;

public abstract class ProductRepository implements BaseRepository<Product, String, List<Product>> {



    protected abstract List<Product> getListByCategoryId(UUID categoryId);

    protected abstract List<Product> getListBySellerId(UUID sellerId);


}

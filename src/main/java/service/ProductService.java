package service;

import jsonFile.CollectionsTypeFactory;
import jsonFile.FileUrls;
import jsonFile.FileUtils;
import jsonFile.Json;
import lombok.SneakyThrows;
import model.Product;
import repository.ProductRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ProductService extends ProductRepository {
    @Override
    public Product get(UUID productId) {
        for (Product product : getProductListFromFile()) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    @Override
    public List<Product> getList() {
        return getProductListFromFile();
    }

    @Override
    public List<Product> getList(UUID categoryOrSellerId) { // ishlatma bu function ni
        return null;
    }

    @Override
    public String add(Product newProduct) {//Agar maxsulot mavjud bo'lsa, sonini oshiritb qoyadi va narxi yangi maxsulot kabi bo'lib qoladi;
        List<Product> productList = getProductListFromFile();
        int cnt=0;
        for (Product product : productList) {
            if (product.getName() != null && product.getName().equals(newProduct.getName())) {
                cnt++;
            }
        }
        if (cnt==0){
            productList.add(newProduct);
            setProductListToFile(productList);
            return SUCCESS;
        }
        return ERROR_PRODUCT_ALREADY_EXIST;
    }

    @Override
    public String editById(UUID id, Product editingProduct) { // NOT USED
        List<Product> productList = getProductListFromFile();
        for (Product product : productList) {
            if (product.getId().equals(id)) {
                if (editingProduct.getProductInfo() != null) {
                    product.setProductInfo(editingProduct.getProductInfo());
                }
                if (editingProduct.getAmount() != 0) {
                    product.setAmount(editingProduct.getAmount());
                }
                if (editingProduct.getPrice() != 0) {
                    product.setAmount(editingProduct.getAmount());
                }
                if (editingProduct.getName() != null) {
                    product.setName(editingProduct.getName());
                }
                product.setUpdatedDate(editingProduct.getCreatedDate());

                setProductListToFile(productList);
                return SUCCESS;
            }
        }
        return ERROR_PRODUCT_NOT_FOUND;
    }

    @Override
    public List<Product> getListByCategoryId(UUID categoryId) {
        List<Product> products = new ArrayList<>();
        for (Product product : getProductListFromFile()) {
            if (product.getCategoryId().equals(categoryId)) {
                products.add(product);
            }
        }
        return products;
    }

    @Override
    protected List<Product> getListBySellerId(UUID sellerId) {
        List<Product> products = new ArrayList<>();
        for (Product product : getProductListFromFile()) {
            if (product.getSellerId().equals(sellerId)) {
                products.add(product);
            }
        }
        return products;
    }

    public List<Product> getProductListFromFile() {
        String productJsonStringFromFile = FileUtils.readFromFile(FileUrls.productUrl);
        List<Product> productList;
        try {
            productList = Json.objectMapper.readValue(productJsonStringFromFile, CollectionsTypeFactory.listOf(Product.class));
        } catch (Exception e) {
            System.out.println(e);
            productList = new ArrayList<>();
        }
        return productList;
    }

    @SneakyThrows
    public void setProductListToFile(List<Product> productList) {
        String newProductJsonFromObject = Json.prettyPrint(productList);
        FileUtils.writeToFile(FileUrls.productUrl, newProductJsonFromObject);
    }

    public String editBySellerName(Product editedProduct) {
        List<Product> productList = getProductListFromFile();
        int index = 0;
        for (Product product : productList) {
            if (product.getName().equals(editedProduct.getName())) {
                if (editedProduct.getName() != null)
                    product.setName(editedProduct.getName());

                if (editedProduct.getPrice() != 0)
                    product.setPrice(editedProduct.getPrice());

                if (editedProduct.getProductInfo() != null)
                    product.setProductInfo(editedProduct.getProductInfo());

                if (editedProduct.getFileUrlPhoto() != null)
                    product.setFileUrlPhoto(editedProduct.getFileUrlPhoto());

                product.setActive(editedProduct.isActive());

                productList.set(index, product);
                setProductListToFile(productList);
                return SUCCESS;
            }
            index++;
        }
        return ERROR_USER_NOT_FOUND;
    }

    public String editByProductName(String name, Product editedProduct) {
        List<Product> productList = getProductListFromFile();
        int index = 0;
        for (Product product : productList) {
            if (product.getName().equals(name)) {
                if (editedProduct.getPrice() != 0)
                    product.setPrice(editedProduct.getPrice());

                if (editedProduct.getProductInfo() != null)
                    product.setProductInfo(editedProduct.getProductInfo());

                if (editedProduct.getFileUrlPhoto() != null)
                    product.setFileUrlPhoto(editedProduct.getFileUrlPhoto());

                product.setActive(editedProduct.isActive());

                productList.set(index, product);
                setProductListToFile(productList);
                return SUCCESS;
            }
            index++;
        }
        return ERROR_USER_NOT_FOUND;
    }

    public Product checkProduct(String productName) {

        for (Product product : getProductListFromFile()) {
            if (product.getName() != null && product.getName().equals(productName)) {
                return product;
            }
        }
        return null;
    }
}

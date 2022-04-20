package service;

import jsonFile.CollectionsTypeFactory;
import jsonFile.FileUrls;
import jsonFile.FileUtils;
import jsonFile.Json;
import lombok.SneakyThrows;
import model.Category;
import repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryService extends CategoryRepository {

    @Override
    public Category get(UUID categoryId) {
        for (Category category : getCategoryListFromFile()) {
            if (category.getId().equals(categoryId))
                return category;
        }
        return null;
    }

    @Override
    public List<Category> getList() {
        return getCategoryListFromFile();
    }

    public Category getByCategoryName(String categoryName) {
        for (Category category : getCategoryListFromFile()) {
            if (category.getName().equals(categoryName)) {
                return category;
            }
        }
        return null;
    }

    @Override
    public List<Category> getList(UUID id) {
        return null;
    }

    @Override
    public String add(Category category) {
        if (isCategoryExist(category.getName())){
            return ERROR_CATEGORY_ALREADY_EXIST;
        }

        List<Category> categoryList = getCategoryListFromFile();
        categoryList.add(category);

        setCategoryListToFile(categoryList);
        return SUCCESS;
    }

    @Override
    public String editById(UUID categoryId, Category category) {
        List<Category> categoryList = getCategoryListFromFile();

        for (Category existCategory : categoryList) {
            if (existCategory.getId().equals(categoryId)) {
                existCategory.setName(category.getName());
                existCategory.setActive(category.isActive());
                existCategory.setCreatedBy(category.getCreatedBy());
                existCategory.setUpdatedBy(category.getUpdatedBy());
                existCategory.setCreatedDate(category.getCreatedDate());
                existCategory.setUpdatedDate(category.getUpdatedDate());

                setCategoryListToFile(categoryList);
                return SUCCESS;
            }
        }
        return ERROR_USER_NOT_FOUND;
    }

    public boolean isCategoryExist(String categoryName) {
        for (Category category : getCategoryListFromFile()) {
            if (category.getName().equals(categoryName)) {
                return true;
            }
        }
        return false;
    }

    public List<Category> getCategoryListFromFile() {
        String categoryJsonStringFromFile = FileUtils.readFromFile(FileUrls.categoryUrl);
        List<Category> categoryList;
        try {
            categoryList = Json.objectMapper.readValue(categoryJsonStringFromFile, CollectionsTypeFactory.listOf(Category.class));
        } catch (Exception e) {
            System.out.println(e);
            categoryList = new ArrayList<>();
        }
        return categoryList;
    }

    @SneakyThrows
    public void setCategoryListToFile(List<Category> categoryList) {
        String newCategoryJsonFromObject = Json.prettyPrint(categoryList);
        FileUtils.writeToFile(FileUrls.categoryUrl, newCategoryJsonFromObject);
    }
}

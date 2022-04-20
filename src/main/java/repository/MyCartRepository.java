package repository;

import model.MyCart;

import java.util.List;
import java.util.UUID;

public abstract class MyCartRepository implements BaseRepository<MyCart, String, List<MyCart>> {

    protected abstract String buy(List<MyCart> myCartList);

    protected abstract double myPurchase(UUID userID);
}

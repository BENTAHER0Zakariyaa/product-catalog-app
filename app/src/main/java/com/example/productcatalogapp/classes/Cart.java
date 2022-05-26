package com.example.productcatalogapp.classes;

import java.util.ArrayList;

public class Cart {

    public ArrayList<CartLine> cartLines;

    public Cart(){
        this.cartLines = new ArrayList<CartLine>();
    }

    public void addCartLine(CartLine cartLine){
        Integer position = isCardLineExist(cartLine.getProduct().getId());
        if (position != -1){
            cartLines.get(position).setQuantity(cartLines.get(position).getQuantity() + cartLine.getQuantity());
        }else {
            cartLines.add(cartLine);
        }
    }

    public void removeCartLine(Integer productId){
        Integer position = isCardLineExist(productId);
        if (position != -1){
            isCardLineExist(productId);
        }
    }

    public CartLine getCartLine(Integer lineId){
        return cartLines.get(lineId);
    }

    public Integer isCardLineExist(Integer productId){
        Integer position = -1;
        for (int i = 0; i < cartLines.size(); i++) {
            if (productId == cartLines.get(i).getProduct().getId()){
                position = i;
                break;
            }
        }
        return position;
    }

    public float getTotal(){
        float total = 0;
        for (int i = 0; i < cartLines.size(); i++) {
            total+=cartLines.get(i).getTotalPrice();
        }
        return total;
    }

    public int getCount() {
        return this.cartLines.size();
    }
}

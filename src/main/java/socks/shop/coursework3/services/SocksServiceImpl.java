package socks.shop.coursework3.services;

import org.springframework.stereotype.Service;
import socks.shop.coursework3.models.Socks;

import java.util.HashMap;

@Service
public class SocksServiceImpl implements SocksService{
    private final HashMap<Socks, Socks> socksStorage = new HashMap<>();
    private final HashMap<Socks, Socks> damagedSocksStorage = new HashMap<>();

    @Override
    public Socks addSocks(Socks newSocks) {
       if (!socksStorage.containsKey(newSocks)) {
           socksStorage.put(newSocks, newSocks);
           return newSocks;
       } else {
           Socks socks = socksStorage.get(newSocks);
           socks.setQuantity(socks.getQuantity() + newSocks.getQuantity());
           socksStorage.put(socks, socks);
           return socks;
       }
    }
    @Override
    public Socks releaseSocks(Socks newSocks) {
        if(socksStorage.get(newSocks).getQuantity() >= newSocks.getQuantity()) {
            Socks socks = socksStorage.get(newSocks);
            socks.setQuantity(socks.getQuantity() - newSocks.getQuantity());
            socksStorage.put(socks, socks);
            return socks;
        } else {
            return null;
        }
    }

    @Override
    public int filterByColorSizeCottonPart(String color, int size, int minCottonPart, int maxCottonPart) {
        int quantity = 0;
        for (Socks socks : socksStorage.keySet()) {
            if (socks.getColor().color.equals(color) && socks.getSize().size == size &&
                    socks.getCottonPart() >= minCottonPart && socks.getCottonPart() <= maxCottonPart);{
                  quantity = socks.getQuantity();
            }
        } return quantity;
    }

    @Override
    public Socks deleteDamagedSocks(Socks newSocks) {
        if(socksStorage.get(newSocks).getQuantity() >= newSocks.getQuantity()) {
            Socks socks = socksStorage.get(newSocks);
            socks.setQuantity(socks.getQuantity() - newSocks.getQuantity());
            socksStorage.put(socks, socks);
            damagedSocksStorage.put(newSocks, newSocks);
            return socks;
        } else {
            return null;
        }
    }
}

package socks.shop.coursework3.services;

import socks.shop.coursework3.models.Socks;

public interface SocksService {
    Socks addSocks(Socks newSocks);

    Socks releaseSocks(Socks newSocks);

    int filterByColorSizeCottonPart(String color, int size, int minCottonPart, int maxCottonPart);

    Socks deleteDamagedSocks(Socks newSocks);
}

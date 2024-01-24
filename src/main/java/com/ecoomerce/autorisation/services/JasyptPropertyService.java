package com.ecoomerce.autorisation.services;

public interface JasyptPropertyService {

    String encrypt(String text, String secret, String algo);

    String decrypt(String text, String secret, String algo);
}

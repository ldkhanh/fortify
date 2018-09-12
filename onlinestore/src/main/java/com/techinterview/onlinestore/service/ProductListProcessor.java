package com.techinterview.onlinestore.service;

import com.techinterview.onlinestore.domain.Product;
import com.techinterview.onlinestore.domain.BackPack;
import com.techinterview.onlinestore.domain.SmartPhone;

import java.lang.reflect.*;

import java.util.*;

/**
 * Implement the function productListToString that does conversion of provided list of products to text representation
 * of this list in this way.
 * - Result string is \n - separated string. E.g.
 *
 *   product 1 details
 *   product 2 details
 *   ...
 *   product 3 details
 *
 * - Each line contains details of one product
 * - Product description line should look like this:
 *   Product name (product GUID), product attribute 1 name:product attribute1 value; ... product attribute n name:product attributen value;
 *   For example. List contains BackPack definition:
 *   BackPack {
 *       guid: 111-222-333
 *       name: Cool Backpack
 *       maxContentWeight: 15
 *       color: "Black"
 *       capacity: 20
 *   }
 *   This becka pakc description string should look like this:
 *   Cool Backpack (111-222-333), maxContentWeight: 15, color: "Black", capacity: 20
 *
 * Keep in mind these requirements:
 * - String reorientation can be modified in future.
 * - There is a possibility to support multiply ways to convert list of products to string. E.g. it it is possible that in future
 *   you will need to implement support of different formats of outpust string (e.g. json instead of \n-separated string).
 * The basic idea is to make your current implementation flexible and modifiable in future.
 *
 * You can use any build system to build the sources (maven, gralde, ant).
 * You can use any 3rd party libs in your application.
 * Any java version (>=8).
 * Code must be tested (framework is up to you).
 *
 * If you are familiar with Git, please do work in a separate branch and create a pull request with your changes.
 */
public class ProductListProcessor {

    /**
     * Make String representation of providd product list.
     * @param products list of the products that needs to be converted to String
     * @return String representation of the provided list.
     */
    private final Character Separator = ',';
    private final Character Space = ' ';
    private final Character NewLine = '\n';
    private final Character Semicolon = ':';
    private final Character DoubleQuote = '\"';
    private final Character OpenBr = '(';
    private final Character CloseBr = ')';
    public String productListToString(List<Product> products) {
        StringBuilder sb = new StringBuilder();
        for (Product p : products) {
            sb.append(p.getName()).append(Space);
            sb.append(OpenBr).append(p.getGuid()).append(CloseBr).append(Separator).append(Space);
            sb.append(this.getDesc(p));     // this product processor extensible any type of Product
        }
        return sb.toString();
    }
    // This function can parse description any type of Product into String format
    private String getDesc(Product po) {
        StringBuilder sb = new StringBuilder();
        try {
            Class c = po.getClass();                      // Get class
            Method[] m = c.getDeclaredMethods();          // Get all Method
            for (int i = 0; i < m.length; i++) {
                String medName = m[i].getName();          // Get Method name
                String prefix = medName.substring(0,3);   // Get prefix "get" or "set" ...
                String desName = medName.substring(3);    // Get name of description
                if (prefix.equals("get")) {
                    sb.append( desName).append(Semicolon).append(Space);
                    Object o = m[i].invoke(po);           // call all the getMethod
                    if (m[i].getReturnType().getName().equals("java.lang.String"))    // Check return type is String
                        sb.append(DoubleQuote).append(o).append(DoubleQuote);
                    else 
                        sb.append(o);
                    sb.append(Separator).append(Space);
                }
            }
        } catch (Throwable e) {
            System.err.println(e);
        }
        sb.setLength(sb.length() - 2);  // remove ',' and space in the end
        sb.append(NewLine);

        return sb.toString();
    }
    public static void main(String[] args) {
        BackPack bp = new BackPack("111-222-333", "Cool Backpack");
        bp.setMaxContentWeight(15);
        bp.setColor("Black");
        bp.setCapacity(20);

        SmartPhone sm = new SmartPhone("234-342-233", "iPhone");
        sm.setManufacturer("Apple");
        sm.setColor("Red");
        sm.setNumberOfCPUs(2);
        sm.setRamSize(2);
        sm.setScreenResolution("2000x1980");
        
        List<Product> list = new ArrayList<>();
        list.add(bp);
        list.add(sm);
        ProductListProcessor pr = new ProductListProcessor();
        
        String str = pr.productListToString(list);
        System.out.println(str);

    }
}

package com.mycompany.myproject;

import java.sql.Connection;

public class TESTDB {
    public static void main(String[] args) {
        Connection c = DBConnect.ConnectToDB();
        if (c != null) {
            System.out.println("✅ Connected to rescuetable successfully!");
        } else {
            System.out.println("❌ Connection failed.");
        }
    }
}
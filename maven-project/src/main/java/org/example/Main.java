package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import io.github.kosmx.edu.math.vector3.Vector3D;

public class Main {
    public static void main(String[] args) {

        JsonObject obj = JsonParser.parseString("{\"str\": \"Hello gson!\"}").getAsJsonObject();
        System.out.println(obj.get("str").getAsString());

        /* // Vector 3d test code
        Vector3D a = new Vector3D(4, 0, 0);
        Vector3D b = new Vector3D(0, 5, 0);
        System.out.println(a.cross(b).length());
         */
    }
}
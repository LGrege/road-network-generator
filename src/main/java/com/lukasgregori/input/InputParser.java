/*********************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Lukas Gregori
 * lukas.gregori@student.tugraz.at
 * www.lukasgregori.com
 *
 * (c) 2017 by Lukas Gregori
 *********************************************************************/

package com.lukasgregori.input;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.istack.internal.Nullable;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * @author Lukas Gregori
 */
public class InputParser {

    public static RoadNetworkConfiguration parseInput(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
        Optional<InputStream> opt = Optional.ofNullable(getInputStream(fileName));
        InputStream stream = opt.orElseThrow(FileNotFoundException::new);
        Reader reader = new InputStreamReader(stream);
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(reader, RoadNetworkConfiguration.class);
    }

    @Nullable
    private static InputStream getInputStream(String fileName) {
        ClassLoader classLoader = InputParser.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }
}
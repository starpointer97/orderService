package com.nishit.bitvavo.io.readers;

import java.util.Optional;
import java.util.Scanner;

public class StandardReader implements Reader{
    Scanner scanner;

    public StandardReader(){
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Optional<String> nextLine() {
        if(scanner.hasNextLine()){
            return Optional.of(scanner.nextLine());
        }
        return Optional.empty();
    }
}

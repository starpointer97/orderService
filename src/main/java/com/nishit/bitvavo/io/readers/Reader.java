package com.nishit.bitvavo.io.readers;

import java.util.Optional;

public interface Reader {

    public Optional<String> nextLine();
}

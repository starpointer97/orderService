package com.nishit.bitvavo.io.readers;

import java.util.Optional;

public interface Reader {

    Optional<String> nextLine();
}

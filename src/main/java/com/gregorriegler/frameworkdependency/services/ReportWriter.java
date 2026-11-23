package com.gregorriegler.frameworkdependency.services;

import java.io.IOException;
import java.nio.file.Path;

public interface ReportWriter {

    Path write(String content) throws IOException;
}
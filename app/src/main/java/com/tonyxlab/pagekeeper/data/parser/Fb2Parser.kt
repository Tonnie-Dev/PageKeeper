package com.tonyxlab.pagekeeper.data.parser

import com.tonyxlab.pagekeeper.data.model.ParsedBook
import java.io.File

interface Fb2Parser {
    fun parse(file: File): Result<ParsedBook>
}

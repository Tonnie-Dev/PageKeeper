package com.tonyxlab.pagekeeper.domain.exception

class ItemNotFoundException( id: String): Exception("Item with Id: $id not found")

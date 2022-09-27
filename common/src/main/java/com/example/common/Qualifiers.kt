package com.example.common

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue


class DBDataSource : Qualifier {
    override val value: QualifierValue = "DBDataSource"

    override fun toString(): String {
        return "q:'$value'"
    }
}

class InMemoryDataSource : Qualifier {
    override val value: QualifierValue = "InMemoryDataSource"

    override fun toString(): String {
        return "q:'$value'"
    }
}

class FileDataSource : Qualifier {
    override val value: QualifierValue = "FileDataSource"

    override fun toString(): String {
        return "q:'$value'"
    }
}

class DataStoreDataSource : Qualifier {
    override val value: QualifierValue = "DataStoreDataSource"

    override fun toString(): String {
        return "q:'$value'"
    }
}

class RemoteDataSource : Qualifier {
    override val value: QualifierValue = "RemoteDataSource"

    override fun toString(): String {
        return "q:'$value'"
    }
}

class DefaultRepository : Qualifier {
    override val value: QualifierValue = "DefaultRepository"

    override fun toString(): String {
        return "q:'$value'"
    }
}

class CacheRepository : Qualifier {
    override val value: QualifierValue = "CacheRepository"

    override fun toString(): String {
        return "q:'$value'"
    }
}

class IODispatcher : Qualifier {
    override val value: QualifierValue = "IODispatcher"

    override fun toString(): String {
        return "q:'$value'"
    }
}

class DefaultDispatcher : Qualifier {
    override val value: QualifierValue = "DefaultDispatcher"

    override fun toString(): String {
        return "q:'$value'"
    }
}
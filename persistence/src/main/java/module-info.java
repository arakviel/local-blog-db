open module com.arakviel.localblogdb.persistence {
    requires org.slf4j;
    requires java.sql;
    requires jakarta.annotation;
    requires spring.context;
    requires spring.core;
    requires spring.beans;

    exports com.arakviel.persistence.entity;
    exports com.arakviel.persistence.entity.proxy;
    exports com.arakviel.persistence.entity.filter;
    exports com.arakviel.persistence.context.factory;
    exports com.arakviel.persistence;

    //opens com.arakviel.persistence to spring.beans, spring.core, spring.context;
}
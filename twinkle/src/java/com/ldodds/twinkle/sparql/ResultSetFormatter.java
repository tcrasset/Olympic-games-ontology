package com.ldodds.twinkle.sparql;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.ResultSet;

/**
 * @author ccslrd
 */
public interface ResultSetFormatter
{
    void display(Query query, ResultSet results);
}

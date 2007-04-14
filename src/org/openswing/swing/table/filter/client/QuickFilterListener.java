package org.openswing.swing.table.filter.client;

import org.openswing.swing.table.columns.client.Column;


/**
 * <p>Title: Benetton - Gestione Imballi</p>
 * <p>Description: Ascoltatore di eventi, associato al filtro rapido della griglia.</p>
 * <p>Copyright: Copyright (c) 2005 Benetton spa</p>
 * <p>Company: Tecnoinformatica spa</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public interface QuickFilterListener {

    /**
     * Metodo chiamato dal pannello di filtro per filtrare i dati rispetto ad una colonna
     * @param colProps proprieta' associate alla colonna da filtrare
     * @param value1 valore fisso o valore min
     * @param value2 eventuale valore massimo
     * <br>Nota: nel caso di filtro per valore value1 è valorizzato e value2 è null. Nel
     * caso di filtro per range, value1 contiene il valore minimo, e value2 il valore massimo
     * nel caso di filtro
     */
    public void filter(Column colProps,Object value1,Object value2);

}
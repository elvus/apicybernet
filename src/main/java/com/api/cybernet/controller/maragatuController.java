package com.api.cybernet.controller;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.api.cybernet.models.marangatu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST }, maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class maragatuController {
    @Autowired
	private JdbcTemplate template;

    @RequestMapping("/marangatu")
    String GetPlanilla(@RequestBody Map<String, String> params){ 
        return getPlanillaMarangatu(params.get("timbrado").toString(), params.get("fecha_inicio").toString(), params.get("fecha_fin").toString());
    }

    public String getPlanillaMarangatu(String timbrado, String startDate, String endDate){
        Gson gson = new Gson();
        Type list = new TypeToken<List<marangatu>>() {}.getType();
         List<marangatu> marangatus = template.query("SELECT 1 Tipo_registro,11 TIPO_IDENTICADOR_COMPRADOR,REPLACE(c.RUC,'.','') AS NRO_IDENT_COMPRADOR,CASE f.`Anulada` WHEN TRUE THEN 'ANULADO' ELSE c.`Nombre` END RAZON_SOCIAL,f.`Fecha` AS FECHA_PAGO, '"+timbrado+"' AS timbrado,CONCAT('001-001-', LPAD(f.NFAC, 7, '0')) AS numFactura,c.`Contrato` AS TOTAL_10,0 AS TOTAL_5,0 EXENTO,c.`Contrato` AS TOTAL,1 CONDICION_VENTA,'N' AS MONEDA_EXTRANJERA,'S' IMPUTA_IVA,'N' IMPUTA_IRE,'N' IMPUTA_IRP,'' COMPROBANTE_ASOCIADO,'' TIMBRADO_ASOCIADO FROM `tblNames` c,`Factura` f WHERE c.`IdCliente` = f.`IdCliente` AND c.`Generar factura` IS TRUE AND f.`Anulada` = FALSE AND f.`Fecha` BETWEEN '"+startDate+"' AND '"+ endDate +"' ORDER BY f.`Fecha`", new RowMapper<marangatu>(){
                    @Override
                    public marangatu mapRow(ResultSet rs, int rowNum) throws SQLException {
                        marangatu planilla = new marangatu();
                        planilla.setTipo_registro(rs.getString("Tipo_registro"));
                        planilla.setTipo_identicador_comprador(rs.getString("TIPO_IDENTICADOR_COMPRADOR"));
                        planilla.setNro_ident_comprador(rs.getString("NRO_IDENT_COMPRADOR"));
                        planilla.setRazon_social(rs.getString("RAZON_SOCIAL"));
                        planilla.setFecha_pago(rs.getString("FECHA_PAGO"));
                        planilla.setTimbrado(rs.getString("timbrado"));
                        planilla.setNumfactura(rs.getString("numFactura"));
                        planilla.setTotal_10(rs.getString("TOTAL_10"));
                        planilla.setTotal_5(rs.getString("TOTAL_5"));
                        planilla.setExento(rs.getString("EXENTO"));
                        planilla.setTotal_comprobante(rs.getString("TOTAL"));
                        planilla.setCondicion_venta(rs.getString("CONDICION_VENTA"));
                        planilla.setMoneda_extranjera(rs.getString("MONEDA_EXTRANJERA"));
                        planilla.setImputa_iva(rs.getString("IMPUTA_IVA"));
                        planilla.setImputa_ire(rs.getString("IMPUTA_IRE"));
                        planilla.setImputa_irp(rs.getString("IMPUTA_IRP"));
                        planilla.setComprobante_asociado(rs.getString("COMPROBANTE_ASOCIADO"));
                        planilla.setTimbrado_asociado(rs.getString("TIMBRADO_ASOCIADO"));
                        return planilla;
                    }
                    
                });
        return gson.toJson(marangatus, list);
    }
}

package com.prueba.PruebaConcepto.tenant;

public class TenantContext {

    private static final ThreadLocal<String> currentClinicaId = new ThreadLocal<>();

    private TenantContext() {
        // Evita instanciación
    }

    public static void setClinicaId(String clinicaId) {
        currentClinicaId.set(clinicaId);
    }

    public static String getClinicaId() {
        return currentClinicaId.get();
    }

    public static void clear() {
        currentClinicaId.remove();
    }
}

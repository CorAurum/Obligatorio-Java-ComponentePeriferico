//package com.prueba.PruebaConcepto.tenant;
//
//public class TenantContext {
//
//    private static final ThreadLocal<Long> currentClinicaId = new ThreadLocal<>();
//
//    private TenantContext() {
//        // Evita instanciación
//    }
//
//    public static void setClinicaId(Long clinicaId) {
//        currentClinicaId.set(clinicaId);
//    }
//
//    public static Long getClinicaId() {
//        return currentClinicaId.get();
//    }
//
//    public static void clear() {
//        currentClinicaId.remove();
//    }
//}

//package com.prueba.PruebaConcepto.tenant;
//
//import com.prueba.PruebaConcepto.entity.Clinica;
//import com.prueba.PruebaConcepto.repository.ClinicaRepository;
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class TenantFilter implements Filter {
//
//    @Autowired
//    private ClinicaRepository clinicaRepository;
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest http = (HttpServletRequest) request;
//        String path = http.getRequestURI();
//        String host = http.getServerName(); // ej: centrovida.enbodi.xyz o localhost
//
//        // 🧠 1️⃣ Excluir rutas "globales" sin tenant (por ejemplo /api/clinicas)
//        if (path.startsWith("/api/clinicas")) {
//            // No aplicamos tenant — seguimos sin setear contexto
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // 🧠 2️⃣ Resolver la clínica según el dominio (host)
//        Clinica clinica = clinicaRepository.findByDominioSubdominio(host)
//                .orElseThrow(() -> new ServletException(
//                        "No se encontró una clínica asociada al dominio: " + host));
//
//        try {
//            // 🧠 3️⃣ Guardamos el ID de la clínica activa en el contexto
//            TenantContext.setClinicaId(clinica.getId());
//            chain.doFilter(request, response);
//        } finally {
//            // 🧠 4️⃣ Limpiamos el contexto para evitar fugas entre hilos
//            TenantContext.clear();
//        }
//    }
//}

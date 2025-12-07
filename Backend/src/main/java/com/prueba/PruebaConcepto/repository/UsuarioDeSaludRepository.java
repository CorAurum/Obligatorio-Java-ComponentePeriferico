package com.prueba.PruebaConcepto.repository;

import com.prueba.PruebaConcepto.entity.UsuarioDeSalud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioDeSaludRepository extends JpaRepository<UsuarioDeSalud, Long> {
        List<UsuarioDeSalud> findByClinicaId(String clinicaId);

        Optional<UsuarioDeSalud> findByIdAndClinicaId(Long id, String clinicaId);

        Optional<UsuarioDeSalud> findByEmail(String email);

        @Query("""
                            select case when count(i) > 0 then true else false end
                            from IdentificadorUsuario i
                            where lower(i.tipo) = lower(:tipo)
                              and lower(i.valor) = lower(:valor)
                              and i.usuario.clinica.id = :clinicaId
                        """)
        boolean existsIdentificadorInClinica(
                        @Param("clinicaId") String clinicaId,
                        @Param("tipo") String tipo,
                        @Param("valor") String valor);

        @Query("""
                            select case when count(i) > 0 then true else false end
                            from IdentificadorUsuario i
                            where lower(i.tipo) = lower(:tipo)
                              and lower(i.valor) = lower(:valor)
                              and i.usuario.clinica.id = :clinicaId
                              and i.usuario.id <> :usuarioId
                        """)
        boolean existsIdentificadorInClinicaExcludingUsuario(
                        @Param("clinicaId") String clinicaId,
                        @Param("tipo") String tipo,
                        @Param("valor") String valor,
                        @Param("usuarioId") Long usuarioId);
}
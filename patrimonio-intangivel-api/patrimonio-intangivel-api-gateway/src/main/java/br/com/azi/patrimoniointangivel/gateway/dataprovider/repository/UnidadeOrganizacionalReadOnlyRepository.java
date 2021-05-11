package br.com.azi.patrimoniointangivel.gateway.dataprovider.repository;

import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.UnidadeOrganizacionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeOrganizacionalReadOnlyRepository extends JpaRepository<UnidadeOrganizacionalEntity, Long> {

}

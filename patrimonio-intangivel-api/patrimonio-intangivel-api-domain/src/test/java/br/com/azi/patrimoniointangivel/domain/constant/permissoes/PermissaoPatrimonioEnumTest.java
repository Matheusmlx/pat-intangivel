package br.com.azi.patrimoniointangivel.domain.constant.permissoes;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PermissaoPatrimonioEnumTest{

    @Test
    public void deveTerNormalEConsultaEnum() {
        Assert.assertEquals("Patrimonio.Normal", PermissaoPatrimonioConstants.NORMAL.getDescription());
        Assert.assertEquals("Patrimonio.Consulta", PermissaoPatrimonioConstants.CONSULTA.getDescription());
    }
}

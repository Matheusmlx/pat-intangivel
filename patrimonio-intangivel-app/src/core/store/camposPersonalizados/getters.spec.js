import getters from './getters'
import camposPersonalizadosDefault from './camposPersonalizadosDefault'

describe('getters', () => {
    let windowSpy

    beforeEach(() => {
        windowSpy = jest.spyOn(global, 'window', 'get')
        windowSpy.mockImplementation(() => ({
            $i18n: {
                locale: 'camposPersonalizados',
                getLocaleMessage: () => camposPersonalizadosDefault.i18n
            }
        }))
    })

    const tipoCampo = 'nome', nomeCampo = 'Nome Intangivel', tipoCampo2 = 'orgao', tipoTela = 'DADOS_DE_ENTRADA_EDICAO'
    const state = (tipo = tipoCampo, nome = nomeCampo, obrigatorio = true) => ({
        camposPersonalizados: camposPersonalizadosDefault.i18n
    })

    const dadosDeEntrada = (nome = 'Patrimonio Intangível') => ({
        nome,
        orgao: 'Prefeitura',
        setor: 'secretaria'
    })

    it('deve retornar os camposPersonalizados do state', () => {
        const camposPersonzalidos = getters.getCamposPersonalizados()
        expect(camposPersonzalidos).toEqual(camposPersonalizadosDefault.i18n)
    })

    it('deve retornar o campo pelo nome do tipo', () => {
        const telaNome = 'DOCUMENTOS'
        const campoLabel = getters.getNomeCamposPersonalizados()('numero', telaNome)
        expect(campoLabel).toEqual(camposPersonalizadosDefault.i18n.DOCUMENTOS.campos.numero.nome)
    })

    it('deve retornar o campo vazio quando não for encontrado', () => {
        const tipo = 'DOCUMENTOS'
        const campoLabel = getters.getNomeCamposPersonalizados()('Qualquer', tipo)
        expect(campoLabel).toBeUndefined()
    })

    it('deve retornar o obrigatorio do campo', () => {
        const campoNome = 'nome'
        const telaNome = 'DADOS_DE_ENTRADA_EDICAO'
        const campoRequired = getters.getObrigatorioCamposPersonalizados()(campoNome, telaNome)
        expect(campoRequired).toEqual(camposPersonalizadosDefault.i18n[telaNome].campos[campoNome].obrigatorio)
    })

    it('deve retornar o tooltip do campo', () => {
        const campoNome = 'Nome'
        const telaNome = 'DADOS_DE_ENTRADA_EDICAO'
        const campoTooltip = getters.getTooltipCamposPersonalizados()(campoNome, telaNome)
        expect(campoTooltip).toEqual(camposPersonalizadosDefault.i18n[telaNome].campos[campoNome])
    })

    it('deve retornar o valido, quando todos os campos forem válidos', () => {
        const valido = getters.getObjetoValidado()(camposPersonalizadosDefault.i18n.DOCUMENTOS.campos, 'DOCUMENTOS')
        expect(valido).toBeTruthy()
    })

    it('deve retornar o não valido, quando algum dos campos forem inválidos', () => {
        const stateAtual = state(tipoCampo, nomeCampo, true)
        const valido = getters.getObjetoValidado(stateAtual)(tipoTela, dadosDeEntrada(''))
        expect(valido).toBeFalsy()
    })
})

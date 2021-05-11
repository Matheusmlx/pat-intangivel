import {mount} from '@vue/test-utils'
import moment from 'moment'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import DadosDeEntrada from './DadosDeEntradaEdicao'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {actionTypes, mutationTypes} from '@/core/constants'
import {patrimonio, patrimonioCompleto, patrimonioIncompleto, patrimonioNL} from './patrimonios'
import camposPersonalizados from '@/core/store/camposPersonalizados'
import camposPersonalizadosDefault from '@/core/store/camposPersonalizados/camposPersonalizadosDefault'

jest.mock('vue-i18n')

describe('DadosDeEntrada', async () => {
    let wrapper, localVue, router, state, mutations, $validator, actions, errors, vuetify, $options, $emit = jest.fn(),
        windowSpy, $gtag

    $validator = {
        _base: {
            validateAll: jest.fn().mockReturnValue(true),
            errors: {
                clear: jest.fn(),
            },
        },
    }
    $options = {
        filters: {
            fornecedorFilter: jest.fn().mockReturnValue(true),
        }
    }
    localVue = applicationTestBuilder.build()
    vuetify = applicationTestBuilder.getVuetify()

    errors = {
        collect: jest.fn(),
    }

    router = {
        init: jest.fn(),
        push: jest.fn(),
        replace: jest.fn(),
        history: {
            current: {
                name: 'DadosDeEntradaEdicao',
                params: {
                    id: patrimonio.id,
                }
            }
        }
    }

    beforeEach(async () => {
        state = {
            loki: {
                timezone: 'America/Cuiaba',
                user: {
                    domainId: 1,
                    domains: [],
                    type: 'INTERNO',
                    authorities: [
                        {
                            hasAccess: true,
                            name: 'Patrimonio.Normal'
                        },
                        {
                            hasAccess: true,
                            name: 'Patrimonio.Retroativo'
                        },
                    ]
                }
            },
            camposAceitos:true,
            salvamentoAutomatico: {
                salvando: false,
            },
            patrimonio: {
                situacaoVindora: '',
            },
            parametros: {
                vidaUtilSemLicenca: 36
            }
        }

        $gtag = {
            event: jest.fn()
        }

        actions = {
            [actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO]: jest.fn(),
            [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonio),
            [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]: jest.fn().mockReturnValue(patrimonio),
            [actionTypes.COMUM.BUSCAR_SETORES]: jest.fn().mockReturnValue(setores),
            [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
            [actionTypes.COMUM.BUSCAR_FORNECEDORES]: jest.fn().mockReturnValue(fornecedores),
            [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue(fornecedorPorId),
            [actionTypes.PATRIMONIO.DELETAR_PATRIMONIO]: jest.fn(),
            [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]: jest.fn()
        }

        mutations = {
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_PATRIMONIO]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_PATRIMONIO_BACKUP]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn()
        }

        windowSpy = jest.spyOn(global, 'window', 'get')
        windowSpy.mockImplementation(() => ({
            location: jest.fn(),
            getComputedStyle: jest.fn(),
            addEventListener: jest.fn(),
            removeEventListener: jest.fn(),
            $i18n: {
                locale: 'camposPersonalizados',
                getLocaleMessage: () => camposPersonalizadosDefault.i18n
            }
        }))
    })

    const setores = {
        items: [{id: 13, sigla: 'DFTS', nome: 'Diretoria de Fiscalização e Técnicas Setoriais'}]
    }

    const orgaos = {
        items: [{id: 5, sigla: 'AGEREG', nome: 'Agência de Regulação dos Serviços Públicos Delegados de Campo Grande'}]
    }

    const fornecedorPorId = [{id: 0, cpfCnpj: '46456456', nome: 'cnpj 0'}]

    const fornecedores = {
        items: [
            {id: 0, cpfCnpj: '46456456', nome: 'cnpj 0'},
            {id: 0, cpfCnpj: '46456456', nome: 'cnpj 0'}
        ]
    }

    const defaultMount = () => mount(DadosDeEntrada, {
        localVue,
        router,
        vuetify,
        store: new Vuex.Store({
            state, mutations, actions,
            modules: {CamposPersonalizados}
        }),
        mocks: {$validator, errors, $emit, $options, $gtag},
    })

    const CamposPersonalizados = {
        namespaced: true,
        state: {
            camposPersonalizados: camposPersonalizadosDefault.i18n
        },
        actions: {getAllCamposPersonlizados: jest.fn()},
        getters: {
            getObjetoValidado: camposPersonalizados.getters.getObjetoValidado,
            getNomeCamposPersonalizados: () => jest.fn(),
            getObrigatorioCamposPersonalizados: () => jest.fn(),
            getTooltipCamposPersonalizados: () => jest.fn()
        }
    }

    describe('Methods', async () => {
        it('Deve atualizar o patrimonio', async () => {
            wrapper = defaultMount()
            wrapper.vm.dadosDeEntrada = patrimonio
            wrapper.vm.salvarFormulario()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO].mock.calls[0][1]).toEqual(patrimonio)
            expect(mutations[mutationTypes.PATRIMONIO.SET_PATRIMONIO]).toBeDefined()
        })

        it('Deve buscar o patrimonio para edicao', async () => {
            wrapper = defaultMount()
            wrapper.vm.buscaPatrimonio()

            expect(actions[actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]).toHaveBeenCalled()
            expect(mutations[mutationTypes.PATRIMONIO.SET_PATRIMONIO]).toBeDefined()
        })

        it('Deve selecionar o orgao e setor caso a lista de orgãos e setores conterem apenas um orgao e um setor', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.dadosDeEntrada.orgao).toEqual(5)
            expect(wrapper.vm.dadosDeEntrada.setor.sigla).toEqual('DFTS')
        })

        it('Deve setar setor como null ao mudar o orgao', async () => {
            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.selecionaOrgao()

            expect(wrapper.vm.dadosDeEntrada.setor).toEqual(null)
            expect(wrapper.vm.setores).toEqual(null)
        })

        it('Deve buscar fornecedor na api pelo cnpj', async () => {
            wrapper = defaultMount()
            wrapper.vm.buscaFornecedorDinamicamente = '46456456'
            await flushPromises()

            expect(actions[actionTypes.COMUM.BUSCAR_FORNECEDORES]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_FORNECEDORES].mock.calls[0][1]).toEqual('46456456')
        })

        it('Deve setar Vencimento da licença como nulo ao mudar reconhecimento para geração interna', async () => {

            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.dadosDeEntrada.dataVencimento = '2020-02-13 03:00:00.000000'
            wrapper.vm.dadosDeEntrada.reconhecimento = 'GERACAO_INTERNA'
            wrapper.vm.resetarEBloquearVencimentoLicenca()

            expect(wrapper.vm.dadosDeEntrada.dataVencimento).toEqual(null)
            expect(wrapper.vm.reconhecimentoSelecionado).toEqual(true)
        })

        it('Deve setar Vencimento da licença como nulo ao mudar reconhecimento para transação sem contraprestação', async () => {

            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.dadosDeEntrada.dataVencimento = '2020-02-13 03:00:00.000000'
            wrapper.vm.dadosDeEntrada.reconhecimento = 'TRANSACAO_SEM_CONTRAPRESTACAO'
            wrapper.vm.resetarEBloquearVencimentoLicenca()

            expect(wrapper.vm.dadosDeEntrada.dataVencimento).toEqual(null)
            expect(wrapper.vm.reconhecimentoSelecionado).toEqual(true)
        })

        it('Deve setar Vencimento da licença como nulo ao setar vida útil indefinida como true', async () => {

            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.dadosDeEntrada.dataVencimento = '2020-02-13 03:00:00.000000'
            wrapper.vm.dadosDeEntrada.vidaIndefinida = true
            wrapper.vm.tratarEventoAnularData()

            expect(wrapper.vm.dadosDeEntrada.dataVencimento).toEqual(null)
            expect(actions[actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]).toHaveBeenCalled()
        })

        it('Deve buscar todos os setores relacionados ao orgão escolhido', async () => {

            wrapper = defaultMount()

            wrapper.vm.dadosDeEntrada.orgao = 30
            wrapper.vm.selecionaOrgao()
            await flushPromises()

            expect(actions[actionTypes.COMUM.BUSCAR_SETORES].mock.calls[0][1]).toEqual(30)
        })

        it('Deve setar setor como nulo ao mudar o orgão escolhido', async () => {

            wrapper = defaultMount()

            await flushPromises()
            wrapper.vm.dadosDeEntrada.setor = {id: 0, sigla: 'DAS', nome: 'Diretoria de Atendimento e Suporte'}
            wrapper.vm.selecionaOrgao()

            expect(wrapper.vm.dadosDeEntrada.setor).toEqual(null)
        })

        it('Deve liberar o acesso a documentos quando todos os estado for em desenvolvimento e validacaoHabilitaEmDesenvolvimento estiver ativa', async () => {
            actions = {
                [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonioCompleto),
                [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]: jest.fn().mockReturnValue(patrimonio),
                [actionTypes.COMUM.BUSCAR_SETORES]: jest.fn().mockReturnValue(setores),
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
            }

            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.dadosDeEntrada.estado = 'EM_DESENVOLVIMENTO'

            await wrapper.vm.salvarFormulario()

            expect(wrapper.vm.camposAceitos).toEqual(true)
            expect(wrapper.emitted('habilitaPasso3')).toBeTruthy()
        })

        it('Não deve liberar o botão continuar quando o estado for em desenvolvimento e o objeto não for valido', async () => {
            actions = {
                [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonioCompleto),
                [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]: jest.fn().mockReturnValue(patrimonio),
                [actionTypes.COMUM.BUSCAR_SETORES]: jest.fn().mockReturnValue(setores),
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
            }

            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.dadosDeEntrada.estado = 'EM_DESENVOLVIMENTO'
            wrapper.vm.alteraDadosDeAcordoComASituacao()
            wrapper.vm.dadosDeEntrada.orgao = null

            await wrapper.vm.salvarFormulario()

            expect(wrapper.vm.camposAceitos).toEqual(false)
            expect(wrapper.emitted('desabilitaPasso3')).toBeTruthy()
        })

        it('deve liberar quando a aquisição for separada e dataVencimentoEVidaIndefinida for verdadeiro, o objeto for valido e validaDataAtivacaoRetroativa for verdadeira', async () => {
            actions = {
                [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonioCompleto),
                [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]: jest.fn().mockReturnValue(patrimonio),
                [actionTypes.COMUM.BUSCAR_SETORES]: jest.fn().mockReturnValue(setores),
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue({id: 1}),
            }

            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.dadosDeEntrada.reconhecimento = 'AQUISICAO_SEPARADA'

            await wrapper.vm.salvarFormulario()

            expect(wrapper.vm.camposAceitos).toEqual(true)
            expect(wrapper.emitted('habilitaPasso3')).toBeTruthy()
        })

        it('deve liberar quando a aquisição não for separada, o objeto for valido e validaDataAtivacaoRetroativa for verdadeira', async () => {
            actions = {
                [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonioCompleto),
                [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]: jest.fn().mockReturnValue(patrimonio),
                [actionTypes.COMUM.BUSCAR_SETORES]: jest.fn().mockReturnValue(setores),
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue({id: 1}),
            }

            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.dadosDeEntrada.reconhecimento = 'QUALQUER_OUTRO'

            await wrapper.vm.salvarFormulario()

            expect(wrapper.vm.camposAceitos).toEqual(true)
            expect(wrapper.emitted('habilitaPasso3')).toBeTruthy()
        })

        it('NÃO deve liberar quando o objeto não for valido', async () => {
            actions = {
                [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonioIncompleto),
                [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]: jest.fn().mockReturnValue(patrimonioIncompleto),
                [actionTypes.COMUM.BUSCAR_SETORES]: jest.fn().mockReturnValue(setores),
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue({id: 1}),
            }

            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.dadosDeEntrada.reconhecimento = 'QUALQUER_OUTRO'

            await wrapper.vm.salvarFormulario()

            expect(wrapper.vm.camposAceitos).toEqual(false)
            expect(wrapper.emitted('desabilitaPasso3')).toBeTruthy()
        })

        it('Deve bloquear acesso a documentos quando numeroNL for menor que 12', async () => {
            actions = {
                [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonioNL),
                [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]: jest.fn().mockReturnValue(patrimonioNL),
                [actionTypes.COMUM.BUSCAR_SETORES]: jest.fn().mockReturnValue(setores),
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
            }

            wrapper = defaultMount()

            await wrapper.vm.salvarFormulario()

            expect(wrapper.vm.camposAceitos).toEqual(false)
            expect(wrapper.emitted('desabilitaPasso3')).toBeTruthy()
        })

        it('Deve definir estado em desenvolvimento', async () => {
            wrapper = defaultMount()

            wrapper.vm.dadosDeEntrada.estado = 'EM_DESENVOLVIMENTO'
            wrapper.vm.alteraDadosDeAcordoComASituacao()

            expect(wrapper.vm.dadosDeEntrada.valorAquisicao).toEqual(null)
            expect(wrapper.vm.dadosDeEntrada.dataVencimento).toEqual(null)
            expect(wrapper.vm.dadosDeEntrada.dataAquisicao).toEqual(null)
            expect(wrapper.vm.dadosDeEntrada.dataAtivacao).toEqual(null)
            expect(wrapper.vm.dadosDeEntrada.dataNL).toEqual(null)
            expect(wrapper.vm.dadosDeEntrada.numeroNL).toEqual(null)
            expect(wrapper.vm.dadosDeEntrada.vidaIndefinida).toEqual(false)
            expect(actions[actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO].mock.calls[0][1]).toEqual(wrapper.vm.dadosDeEntrada)
            expect(mutations[mutationTypes.PATRIMONIO.SET_PATRIMONIO]).toBeDefined()
        })

        it('Deve verificar que é um nome', async () => {
            wrapper = defaultMount()
            wrapper.vm.verificaSeValorEhUmNome('nome')

            expect(wrapper.vm.ehUmNome).toEqual(true)
        })

        it('Deve verificar que é um numero', async () => {
            wrapper = defaultMount()
            wrapper.vm.verificaSeValorEhUmNome('123')

            expect(wrapper.vm.ehUmNome).toEqual(false)
        })

        it('Deve verificar que cnpj contem mascara', async () => {
            wrapper = defaultMount()
            wrapper.vm.verificaSeValorContemMascara('18.236.120/0001-58')
            expect(wrapper.vm.cnpjPesquisadoContemMascara).toEqual(true)
        })

        it('Deve verificar que cnpj não contem mascara', async () => {
            wrapper = defaultMount()
            wrapper.vm.verificaSeValorContemMascara('18236120000158')
            expect(wrapper.vm.cnpjPesquisadoContemMascara).toEqual(false)
        })
    })

    describe('Mounted', async () => {
        it('Deve liberar o acesso a documentos quando todos os campos obrigatórios forem preenchidos com patrimonioCompleto', async () => {
            actions = {
                [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonioCompleto),
                [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]: jest.fn().mockReturnValue(patrimonioCompleto),
                [actionTypes.COMUM.BUSCAR_SETORES]: jest.fn().mockReturnValue(setores),
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue({id: 1})
            }

            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.camposAceitos).toEqual(true)
            expect(wrapper.emitted('habilitaPasso3')).toBeTruthy()
        })

        it('Ao buscar deve trazer data de ativação para usuários com permissão retroativa', async () => {
            actions = {
                [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonioCompleto),
                [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]: jest.fn().mockReturnValue(patrimonioCompleto),
                [actionTypes.COMUM.BUSCAR_SETORES]: jest.fn().mockReturnValue(setores),
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue({id: 1})
            }
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.dadosDeEntrada.dataAtivacao).toEqual('2020-02-26 03:00:00.000000')
        })
    })

    describe('Watcher', async () => {
        it('Ao buscar deve setar data de ativação nula para usuários sem permissão retroativa', async () => {
            state.loki.user.authorities = [{
                hasAccess: true,
                name: 'Patrimonio.Normal'
            }, {
                hasAccess: true,
                name: 'Patrimonio.Consultor'
            }]

            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.dadosDeEntrada.dataAtivacao).toEqual(null)
        })

        it('Quando a data de ativação for maior que a data de vencimento, a data de vencimento deve ser anulada', async () => {
            const dataDeAtivacao = moment(new Date(2020, 0, 1)).format('YYYY-MM-DDTHH:mm:ssZZ')
            const dataDeVencimento = moment(new Date(2020, 8, 1)).format('YYYY-MM-DDTHH:mm:ssZZ')
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.dadosDeEntrada.dataAtivacao = dataDeAtivacao
            wrapper.vm.dadosDeEntrada.dataVencimento = dataDeVencimento
            wrapper.vm.dadosDeEntrada.dataAtivacao = moment(dataDeVencimento).add(1, 'day').format('YYYY-MM-DDTHH:mm:ssZZ')
            await wrapper.vm.validarDataDeAtivacaoEVencimento()
            await flushPromises()

            expect(wrapper.vm.dadosDeEntrada.dataAtivacao).toEqual(null)
        })
    })
})

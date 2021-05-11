import {shallowMount} from '@vue/test-utils'
import PatrimonioDetalheCampos from './PatrimonioDetalheCampos'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {actionTypes, mutationTypes} from '@/core/constants'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import {mask} from 'vue-the-mask'

describe('PatrimonioDetalheCampos', () => {
    let wrapper, localVue, router, state, mutations, $validator, actions, errors, vuetify, directives, $t = jest.fn()

    directives = {
        mask: {
            ...mask,
            tokens: {
                ...mask.tokens,
                '*': /./,
            }
        },
    }

    const defaultMount = (stubs) => shallowMount(PatrimonioDetalheCampos, {
        localVue,
        directives,
        router,
        vuetify,
        stubs,
        store: new Vuex.Store({
            state, mutations, actions,
            modules: {
                CamposPersonalizados
            }
        }),
        mocks: {$validator, errors, $t},
        propsData: {
            patrimonio: patrimonio
        },
    })

    const CamposPersonalizados = {
        namespaced: true,
        actions: {getAllCamposPersonlizados: jest.fn()},
        getters: {
            getNomeCamposPersonalizados: () => jest.fn(),
            getObrigatorioCamposPersonalizados: () => jest.fn(),
            getTooltipCamposPersonalizados: () => jest.fn()
        }
    }

    const patrimonio = {
        id: 48,
        nome: 'teste',
        descricao: null,
        situacao: null,
        reconhecimento: null,
        dataAquisicao: null,
        dataNL: null,
        numeroNL: null,
        fornecedor: null,
        dataVencimento: null,
        tipo: 'DIREITOS_AUTORAIS',
        estado: null,
        orgao: 3,
        setor: 4,
    }

    const patrimonioVisualizar = {
        id: 7,
        tipo: 'SOFTWARES',
        nome: 'teste',
        numero: '0000000002',
        descricao: 'edf',
        situacao: 'ATIVO',
        estado: 'PRONTO_PARA_USO',
        valorLiquido: 88.880000,
        valorAquisicao: 88.880000,
        reconhecimento: 'AQUISICAO_SEPARADA',
        dataAquisicao: null,
        dataNL: null,
        numeroNL: null,
        inicioVidaUtil: '2020-03-02T18:07:17.704+0000',
        dataVencimento: '2020-03-12T07:00:00.000+0000',
        mesesVidaUtil: 1,
        fimVidaUtil: '2020-03-12T07:00:00.000+0000',
        dataAtivacao: '2020-03-02T18:07:17.704+0000',
        fornecedor: 1,
        orgao: {
            id: 5,
            sigla: 'AGEREG',
            nome: 'Agência de Regulação dos Serviços Públicos Delegados de Campo Grande'
        },
        setor:
            {
                id: 13,
                sigla: 'DFTS',
                nome: 'Diretoria de Fiscalização e Técnicas Setoriais'
            },
        contaContabil: {
            id: 1,
            codigo: '124110100',
            descricao: 'BENS INTANGIVEIS>SOFTWARE'
        },
        configAmortizacao: {
            id: 2,
            metodo: 'QUOTAS_CONSTANTES',
            vidaUtil: 1,
            situacao: 'ATIVO',
            taxa: 100.000000,
            percentualResidual: 0.000000
        }
    }

    const fornecedorPorId = [
        {
            id: 0,
            cpfCnpj: '46456456',
            nome: 'cnpj 0',
        },
    ]

    const fornecedores = {
        items:[
            {
                id: 0,
                cpfCnpj: '46456456',
                nome: 'cnpj 0',
            },
            {
                id: 0,
                cpfCnpj: '46456456',
                nome: 'cnpj 0',
            },
        ]
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        errors = {
            collect: jest.fn(),
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                },
            },
        }

        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                    authorities: [
                        {
                            hasAccess: true,
                            name: 'Patrimonio.Normal'
                        }
                    ]
                }
            },
            salvamentoAutomatico: {
                salvando: false,
            },
            patrimonio: {
                situacaoVindora: '',
            },
            rota: {
                origem: 'Inicial'
            }
        }

        actions = {
            [actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO]: jest.fn(),
            [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonio),
            [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]: jest.fn().mockReturnValue(patrimonio),
            [actionTypes.COMUM.BUSCAR_FORNECEDORES]: jest.fn().mockReturnValue(fornecedores),
            [actionTypes.PATRIMONIO.REVERTER_ATIVACAO]: jest.fn(),
            [actionTypes.PATRIMONIO.VISUALIZAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonioVisualizar),
            [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID] : jest.fn().mockReturnValue(fornecedorPorId),
        }

        mutations = {
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_PATRIMONIO]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_PATRIMONIO_BACKUP]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn()
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
            },
        }

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'MenuDetalhe',
                    params: {
                        id: patrimonio.id,
                    },
                },
            },
        }
    })

    describe('Methods', () => {
        it('Deve emitir o evento salvar',() => {
            wrapper = defaultMount()
            wrapper.vm.$props.patrimonio = patrimonioVisualizar
            wrapper.vm.tratarEventoSalvar()

            expect(wrapper.vm.fornecedorSelecionado).toEqual(true)
            expect(wrapper.emitted().salvar).toBeTruthy()
            expect(wrapper.emitted().salvar[0][0]).toEqual(patrimonioVisualizar)
        })

        it('Deve buscar fornecedor na api pelo cnpj', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoBuscaFornecedores('46456456')
            await flushPromises()

            expect(actions[actionTypes.COMUM.BUSCAR_FORNECEDORES]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_FORNECEDORES].mock.calls[0][1]).toEqual('46456456')
        })

        it('Deve retornar o texto do período de vida útil',() => {
            wrapper = defaultMount()
            expect(wrapper.vm.retornaTextoPeriodoVidaUtil(
                6,
                3,
                new Date('09/01/2016'),
                new Date('12/07/2016'))).toEqual('01/09/2016 a 07/12/2016')
        })

        it('Deve verificar que é um nome',() => {
            wrapper = defaultMount()
            wrapper.vm.verificaSeValorEUmNome('nome')

            expect(wrapper.vm.eUmNome).toEqual(true)
        })

        it('Deve verificar que é um numero',() => {
            wrapper = defaultMount()
            wrapper.vm.verificaSeValorEUmNome('123')

            expect(wrapper.vm.eUmNome).toEqual(false)
        })

        it('Deve verificar que cnpj contem mascara',() => {
            wrapper = defaultMount()
            wrapper.vm.verificaSeValorContemMascara('18.236.120/0001-58')
            expect(wrapper.vm.cnpjPesquisadoContemMascara).toEqual(true)
        })

        it('Deve verificar que cnpj não contem mascara',() => {
            wrapper = defaultMount()
            wrapper.vm.verificaSeValorContemMascara('18236120000158')

            expect(wrapper.vm.cnpjPesquisadoContemMascara).toEqual(false)
        })
    })

    describe('Watch', () => {
        it('Deve buscar fornecedor na api se o patrimonio tiver o id do fornecedor', async () => {
            wrapper = defaultMount()
            wrapper.vm.$props.patrimonio = patrimonioVisualizar
            await flushPromises()

            expect(actions[actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID].mock.calls[0][1]).toEqual(1)
        })
    })

    describe('filters', () => {
        it('Deve retornar o nome e cnpj do fornecedor',() => {
            wrapper = defaultMount()

            const fornecedores = [
                {
                    id: 1,
                    nome:' fornecedor 1',
                    cpfCnpj: '049728293028'
                },
                {
                    id: 2,
                    nome:' fornecedor 2',
                    cpfCnpj: '049728666666'
                },
            ]
            expect(wrapper.vm.$options.filters.fornecedor(1,fornecedores))
                .toEqual(' fornecedor 1 - 049728293028')
        })

        it('Deve retornar o codigo e descrição da conta contábil',() => {
            wrapper = defaultMount()

            const conta = {
                    id: 1,
                    descricao:' conta 1',
                    codigo: '456456756'
                }

            expect(wrapper.vm.$options.filters.contaContabil(conta))
                .toEqual('45.645.67.56 -  conta 1')
        })
    })
})

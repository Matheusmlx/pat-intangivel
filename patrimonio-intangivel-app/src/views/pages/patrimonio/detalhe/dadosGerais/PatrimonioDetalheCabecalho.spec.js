import {shallowMount} from '@vue/test-utils'
import PatrimonioDetalheCabechalho from './PatrimonioDetalheCabecalho'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {mask} from 'vue-the-mask'


describe('PatrimonioDetalheCabecalho', () => {
    let wrapper, localVue, router, state, mutations, $validator, actions, errors, vuetify,directives,$t = jest.fn()

    directives = {
        mask: {
            ...mask,
            tokens: {
                ...mask.tokens,
                '*': /./,
            }
        },
    }

    const defaultMount = (stubs) => shallowMount(PatrimonioDetalheCabechalho, {
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
            patrimonio: patrimonioVisualizar
        },
    })

    const CamposPersonalizados = {
        namespaced: true,
        actions: {getAllCamposPersonlizados: jest.fn()},
        getters: {
            getNomeCamposPersonalizados: () => jest.fn(),
            getObrigatorioCamposPersonalizados: () => jest.fn(),
        }
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


    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        state = {
            loki: {},
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

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'MenuDetalhe',
                    params: {
                        id: patrimonioVisualizar.id,
                    },
                },
            },
        }
    })


    describe('Methods', () => {
        it('deve emitir o evento de cancelar ativação', () => {
            wrapper = defaultMount()

            wrapper.vm.tratarEventoCancelarAtivacao()

            expect(wrapper.emitted().cancelarAtivacao).toBeTruthy()

        })
    })
})


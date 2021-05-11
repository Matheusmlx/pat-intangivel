import {mount} from '@vue/test-utils'
import AmortizacaoManual from './AmortizacaoManual'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import { actionTypes, mutationTypes} from '@/core/constants'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import camposPersonalizados from '@/core/store/camposPersonalizados'
import camposPersonalizadosDefault from '../../../../core/store/camposPersonalizados/camposPersonalizadosDefault'
jest.mock('vue-i18n')

describe('AmortizacaoManual', () => {
    let wrapper, localVue,$options,  state, mutations, $validator, actions, errors, vuetify,windowSpy, router

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
                name: 'AmortizacaoManual'
            }
        }
    }

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                    authorities: [
                        {
                            hasAccess: true,
                            name: 'ContaContabil.Normal'
                        }
                    ]
                }
            },
            rota: {
                origem: 'Inicial'
            }
        }

        actions = {
            [actionTypes.AMORTIZACAO.GERAR_AMORTIZACAO_MANUAL]: jest.fn().mockReturnValue(patrimonios),
            [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos)
        }

        mutations = {
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn()
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
            },
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

    const orgaos = {
        items: [
            { id: 5, sigla: 'AGEREG', nome: 'Agência de Regulação dos Serviços Públicos Delegados de Campo Grande' }
        ]
    }

    const patrimonios = { items: [{id:1,numero:'0000000001'},{id:2,numero:'0000000002'}]}

    const defaultMount = () => mount(AmortizacaoManual,{
        localVue,
        router,
        vuetify,
        store: new Vuex.Store({
            state, mutations, actions,
            modules: {CamposPersonalizados}
        }),
        mocks: {
            $validator,
            errors,
            $options
        },
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

    describe('Mounted', () =>{
        it('Deve buscar todos os orgãos', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]).toHaveBeenCalled()
            expect(wrapper.vm.orgaos).toEqual(orgaos.items)
        })
    })

    describe('Methods', () =>{
        it('Deve gerar amortização manual', async () => {
            wrapper = defaultMount()
            wrapper.vm.dadosGerais.orgao = 109
             wrapper.vm.dadosGerais.mesReferencia = '2020-04'
            wrapper.vm.tratarEventoSalvar()
            await flushPromises()

            expect(actions[actionTypes.AMORTIZACAO.GERAR_AMORTIZACAO_MANUAL]).toHaveBeenCalled()
            expect(actions[actionTypes.AMORTIZACAO.GERAR_AMORTIZACAO_MANUAL].mock.calls[0][1]).toEqual(wrapper.vm.criaDados())
            expect(wrapper.vm.panel).toEqual(0)
        })

        it('Deve pesquisar patrimonio',() => {
            wrapper = defaultMount()
            wrapper.vm.patrimoniosBackup = patrimonios.items
            wrapper.vm.pesquisarPatrimonio('1')

            expect(wrapper.vm.patrimonios).toEqual([{id:1,numero:'0000000001'}])
        })

        it('Deve habilitar botão executar',() => {
            wrapper = defaultMount()
            wrapper.vm.dadosGerais.orgao = 109
            wrapper.vm.dadosGerais.mesReferencia = '2020-04-08T00:00:00.000-0300'
            wrapper.vm.verificarCamposObrigatorios()

            expect(wrapper.vm.camposAceitos).toEqual(false)
        })

        it('Deve desabilitar botão executar quando incompleto sem orgao',() => {
            wrapper = defaultMount()
            wrapper.vm.dadosGerais.orgao = null
            wrapper.vm.dadosGerais.mesReferencia = '2020-04-08T00:00:00.000-0300'
            wrapper.vm.verificarCamposObrigatorios()

            expect(wrapper.vm.camposAceitos).toEqual(true)
        })

        it('Deve desabilitar botão executar quando incompleto sem mes',() => {
            wrapper = defaultMount()
            wrapper.vm.dadosGerais.orgao = 109
            wrapper.vm.dadosGerais.mesReferencia = null
            wrapper.vm.verificarCamposObrigatorios()

            expect(wrapper.vm.camposAceitos).toEqual(true)
        })

        it('Deve redirecionar para tela de listagem', () => {
            wrapper = defaultMount()

            wrapper.vm.tratarEventoVoltar()

            expect(router.push.mock.calls[0][0]).toEqual({ name: 'Inicial' })
        })
    })

})

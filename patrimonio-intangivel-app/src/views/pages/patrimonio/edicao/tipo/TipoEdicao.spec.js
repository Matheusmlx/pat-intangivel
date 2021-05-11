import {shallowMount} from '@vue/test-utils'
import TipoIntangivelEdicao from './TipoEdicao'
import flushPromises from 'flush-promises'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {actionTypes, mutationTypes} from '@/core/constants'
import Vuex from 'vuex'

describe('TipoEdicao', () => {
    let wrapper, localVue, store, router, state, mutations, $validator, actions, $gtag

    const tipoIntangivel = {
        id: 1,
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                },
            },
        }

        state = {
            loki: {},
            patrimonio: {
                patrimonio: {},
            },
            rota: {
                origem: 'TodosPatrimonios'
            }
        }

        $gtag = {
            event: jest.fn()
        }

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {
                current: {
                    name: 'TipoEdicao',
                    params: {
                        id: tipoIntangivel.id,
                    },
                },
            },
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            ['SET_UPLOADED_FILES']: jest.fn(),
        }
        actions = {
            [actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO]: jest.fn(),
            [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]: jest.fn(),
            [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn(),
        }

        store = new Vuex.Store({ actions, state, mutations })
    })

    describe('mounted', () => {
        describe('Deve inicializar com o tipo de patrimonio cadastrado', () => {
            it('Deve setar o patrimonioId com o id vindo por parametro', () => {
                wrapper = shallowMount(TipoIntangivelEdicao, {
                    store,
                    localVue,
                    router,
                    mocks: {
                        $validator,
                        $gtag
                    },
                })

                expect(wrapper.vm.patrimonioId).toBeDefined()
                expect(wrapper.vm.patrimonioId).toEqual(1)
            })

            it('Deve buscar o patrimonio por id', async () => {
                wrapper = shallowMount(TipoIntangivelEdicao, {
                    localVue,
                    store,
                    router,
                    mocks: {
                        $validator,
                        $gtag
                    },
                })
                await flushPromises()

                expect(actions[actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID].mock.calls[0][1]).toEqual(1)
                expect(wrapper.vm.patrimonio).toBeDefined()
            })
        })
    })

    describe('methods', () => {
        describe('Seleção e atualização de patrimonio', () => {
            it('Deve enviar requisição de atualização de patrimonio', async () => {
                wrapper = shallowMount(TipoIntangivelEdicao, {
                    localVue,
                    store,
                    router,
                    mocks: {
                        $validator,
                        $gtag
                    },
                })

                wrapper.vm.tratarEventoSelecaoTipoBem('SOFTWARES')
                expect(wrapper.vm.patrimonio.tipo).toEqual('SOFTWARES')
                expect(actions[actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO].mock.calls[0][1]).toEqual(wrapper.vm.patrimonio)
            })

            it('Verificar se não esta enviando o patrimonio para atualização', async () => {
                wrapper = shallowMount(TipoIntangivelEdicao, {
                    localVue,
                    store,
                    router,
                    mocks: {
                        $validator,
                        $gtag
                    },
                })

                wrapper.vm.tratarEventoSelecaoTipoBem(undefined)
                expect(wrapper.vm.patrimonio.tipo).toBeFalsy()
            })
        })

        describe('Avancar para a proxima página ', () => {
            it('O Botao deve estar ativado quando o tipo for selecionado', () => {
                wrapper = shallowMount(TipoIntangivelEdicao, {
                    localVue,
                    router,
                    store,
                    mocks: {
                        $validator,
                        $gtag
                    },
                })
                wrapper.vm.tratarEventoSelecaoTipoBem('SOFTWARES')
                expect(wrapper.vm.patrimonio.tipo).toEqual('SOFTWARES')
            })

            it('Deve Seguir para a proxima pagina enviando o id como parametro', async () => {
                wrapper = shallowMount(TipoIntangivelEdicao, {
                    localVue,
                    store,
                    router,
                    mocks: {
                        $validator,
                        $gtag
                    },
                })

                wrapper.vm.tratarEventoContinuar()

                expect(router.push.mock.calls[0][0]).toEqual({
                    name: 'DadosDeEntradaEdicao',
                    params: { id: 1 },
                })
            })
            it('Deve Voltar para tela de listagem ao clicar em voltar ', () => {
                wrapper = shallowMount(TipoIntangivelEdicao, {
                    localVue,
                    store,
                    router,
                    mocks: {
                        $validator,
                        $gtag
                    },
                })
                wrapper.vm.tratarEventoVoltar()
                expect(router.push.mock.calls[0][0]).toEqual({
                    name: store.state.rota.origem,
                })
            })
        })
    })
})

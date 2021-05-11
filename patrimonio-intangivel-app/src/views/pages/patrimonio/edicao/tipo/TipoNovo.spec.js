import {shallowMount} from '@vue/test-utils'
import TipoIntangivel from './TipoNovo'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {actionTypes, mutationTypes} from '@/core/constants'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'

describe('TipoNovo', () => {
    let wrapper, localVue, store, router, state, mutations, $validator, actions, $gtag

    const patrimonio = {
        tipo: 'SOFTWARES'
    }

    const patrimonioRetornoApi = {
        id: 0,
        tipo: 'SOFTWARES',
        situacao: 'EM_ELABORACAO'

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

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: { current: {} },
        }

        $gtag = {
            event: jest.fn()
        }

        state = {
            loki: {},
            patrimonio: {
                patrimonio: {}
            },
            rota: {
                origem: 'TodosPatrimonios'

            }
        }

        actions = {
            [actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO]: jest.fn().mockReturnValue(patrimonioRetornoApi),
        }

        mutations = {
            [mutationTypes.PATRIMONIO.SET_PATRIMONIO]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
        }

        store = new Vuex.Store({ state, mutations, actions })
    })

    describe('methods', () => {
        describe('tratarEventoSelecaoTipoBem', () => {
            it('Deve setar um patrimonio ao clicar em um tipo cadastrado', () => {
                wrapper = shallowMount(TipoIntangivel, {
                    localVue,
                    store,
                    mocks: {
                        $validator,
                        $gtag
                    },
                })

                wrapper.vm.tratarEventoSelecaoTipoBem('SOFTWARES')

                expect(mutations[mutationTypes.PATRIMONIO.SET_PATRIMONIO].mock.calls[0][1]).toEqual(patrimonio)
            })
        })

        it('Deve Voltar para tela de listagem ao clicar em voltar ', () => {
            wrapper = shallowMount(TipoIntangivel, {
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

        it('Deve habilitar o botão de continuar ao selecionar um tipo', () => {
            wrapper = shallowMount(TipoIntangivel, {
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
        })

        it('Deve cadastrar um patrimonio e redirecionar para dados de entrada edição com id do patrimonio', async () => {
            wrapper = shallowMount(TipoIntangivel, {
                localVue,
                store,
                router,
                mocks: {
                    $validator,
                    $gtag
                },
            })

            store.state.patrimonio.patrimonio.tipo = 'SOFTWARES'
            wrapper.vm.tratarEventoContinuar()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO].mock.calls[0][1]).toEqual(patrimonio)
            expect(router.push.mock.calls[0][0]).toEqual({
                name: 'DadosDeEntradaEdicao',
                params: { id: patrimonioRetornoApi.id }
            })
        })

    })
})

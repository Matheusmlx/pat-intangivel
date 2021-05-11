import {shallowMount} from '@vue/test-utils'
import MenuDetalhe from './MenuDetalhe'
import Vuex from 'vuex'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('MenuDetalhe.vue', () => {

    let localVue, wrapper, state, router, store

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        state = {
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
                        id: 1
                    }
                }
            },
            currentRoute: {
                matched: [
                    {},
                    {
                        props: {
                            default: {
                                tabs: 0
                            }
                        }
                    }
                ]
            }
        }

        store = new Vuex.Store({state})
    })

    describe('renderização', () => {

        it('deve mostrar o menu com as opções DADOS GERAIS e MOVIMENTAÇÕES para alterna entre as telas', () => {
            wrapper = shallowMount(MenuDetalhe, {
                localVue,
                router,
                store
            })

            expect(wrapper.findAll('v-tab-stub').at(0).text()).toContain('DADOS GERAIS')
            expect(wrapper.findAll('v-tab-stub').at(1).text()).toContain('MOVIMENTAÇÕES')
        })
    })
})

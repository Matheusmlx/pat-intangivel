import {shallowMount} from '@vue/test-utils'
import RootPage from './RootPage'
import Vuex from 'vuex'
import {mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('RootPage.vue', () => {

    let localVue, wrapper, state, store, mutations, router

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            routes: [],
            push: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: '/'
                }
            }
        }

        state = {
            parametros: {
                codigoChat: 123
            },
            loki: {
                user: {
                    authorities: [
                        {
                            hasAccess: false,
                            name: 'Patrimonio.Consulta'
                        }
                    ]
                }
            }
        }

        mutations = {
            [mutationTypes.COMUM.SET_RETRAIR_MENU]: jest.fn()
        }

        store = new Vuex.Store({state, mutations})
    })

    describe('methods', () => {

        it('deve redirecionar para a página de acesso negado caso o usuário não tenha permissão Patrimonio.Consulta', () => {
            wrapper = shallowMount(RootPage, {
                localVue,
                router,
                store
            })
            wrapper.vm.redirecionaSeAcessoNegado()

            expect(mutations[mutationTypes.COMUM.SET_RETRAIR_MENU]).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'AcessoNegado'})
        })

        it('não deve redirecionar para a página de acesso negado caso o usuário tenha permissão Patrimonio.Consulta', () => {
            state.loki.user.authorities = [{hasAccess: true, name: 'Patrimonio.Consulta'}]
            wrapper = shallowMount(RootPage, {
                localVue,
                router,
                store
            })
            wrapper.vm.redirecionaSeAcessoNegado()

            expect(mutations[mutationTypes.COMUM.SET_RETRAIR_MENU]).not.toHaveBeenCalled()
        })
    })
})

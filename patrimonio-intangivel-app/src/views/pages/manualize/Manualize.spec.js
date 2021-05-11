import {shallowMount} from '@vue/test-utils'
import Manualize from './Manualize'
import Vuex from 'vuex'
import {mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('Manualize.vue', () => {

    let localVue, wrapper, state, store, mutations

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        state = {
            loki: {
                user: {
                    authorities: [
                        {
                            hasAccess: true,
                            name: 'Patrimonio.Normal'
                        },
                        {
                            hasAccess: true,
                            name: 'Patrimonio.Retroativo'
                        },
                        {
                            hasAccess: true,
                            name: 'ContaContabil.Normal'
                        },
                    ]
                }
            }
        }

        mutations = {
            [mutationTypes.COMUM.SET_RETRAIR_MENU]: jest.fn()
        }

        store = new Vuex.Store({state, mutations})
    })

    describe('mounted', () => {

        it('deve chamar a mutation de retrair o menu', () => {
            wrapper = shallowMount(Manualize, {
                localVue,
                store
            })

            expect(mutations[mutationTypes.COMUM.SET_RETRAIR_MENU]).toHaveBeenCalled()
        })
    })

    describe('methods', () => {

        it('deve retornar true quando o usuário tiver permissão Patrimonio.Retroativo', () => {
            wrapper = shallowMount(Manualize, {
                localVue,
                store
            })
            wrapper.vm.verificaPermissaoRetroativa()

            expect(wrapper.vm.verificaPermissaoRetroativa()).toEqual(true)
        })

        it('deve retornar false quando o usuário não tiver permissão Patrimonio.Retroativo', () => {
            state.loki.user.authorities = [{hasAccess: false, name: 'Patrimonio.Retroativo'}]
            wrapper = shallowMount(Manualize, {
                localVue,
                store
            })
            wrapper.vm.verificaPermissaoRetroativa()

            expect(wrapper.vm.verificaPermissaoRetroativa()).toEqual(false)
        })

        it('deve retornar true quando o usuário tiver permissão Patrimonio.Normal', () => {
            wrapper = shallowMount(Manualize, {
                localVue,
                store
            })
            wrapper.vm.verificaPermissaoEdicao()

            expect(wrapper.vm.verificaPermissaoEdicao()).toEqual(true)
        })

        it('deve retornar false quando o usuário não tiver permissão Patrimonio.Normal', () => {
            state.loki.user.authorities = [{hasAccess: false, name: 'Patrimonio.Normal'}]
            wrapper = shallowMount(Manualize, {
                localVue,
                store
            })
            wrapper.vm.verificaPermissaoEdicao()

            expect(wrapper.vm.verificaPermissaoEdicao()).toEqual(false)
        })

        it('deve retornar true quando o usuário tiver permissão ContaContabil.Normal', () => {
            wrapper = shallowMount(Manualize, {
                localVue,
                store
            })
            wrapper.vm.verificaPermissaoConfiguracao()

            expect(wrapper.vm.verificaPermissaoConfiguracao()).toEqual(true)
        })

        it('deve retornar false quando o usuário não tiver permissão ContaContabil.Normal', () => {
            state.loki.user.authorities = [{hasAccess: false, name: 'ContaContabil.Normal'}]
            wrapper = shallowMount(Manualize, {
                localVue,
                store
            })
            wrapper.vm.verificaPermissaoConfiguracao()

            expect(wrapper.vm.verificaPermissaoConfiguracao()).toEqual(false)
        })
    })

    describe('computed', () => {

        it('deve retornar url retroativo quando o usuário tiver permissão patrimônio retroativo e normal ', () => {
            state.loki.user.authorities = [
                {hasAccess: true, name: 'Patrimonio.Normal'},
                {hasAccess: true, name: 'Patrimonio.Retroativo'},
                {hasAccess: false, name: 'ContaContabil.Normal'}
            ]
            wrapper = shallowMount(Manualize, {
                localVue,
                store
            })

            expect(wrapper.vm.manualizeURL).toEqual('https://manualize.com.br/categoria_patrimonio_intangivel/retroativo')
        })

        it('deve retornar url servidor quando o usuário tiver permissão apenas patrimônio normal', () => {
            state.loki.user.authorities = [
                {hasAccess: true, name: 'Patrimonio.Normal'},
                {hasAccess: false, name: 'Patrimonio.Retroativo'},
                {hasAccess: false, name: 'ContaContabil.Normal'}
            ]
            wrapper = shallowMount(Manualize, {
                localVue,
                store
            })

            expect(wrapper.vm.manualizeURL).toEqual('https://manualize.com.br/categoria_patrimonio_intangivel/servidor/')
        })

        it('deve retornar url administrador quando o usuário tiver permissão apenas patrimônio retroativo ', () => {
            state.loki.user.authorities = [
                {hasAccess: false, name: 'Patrimonio.Normal'},
                {hasAccess: true, name: 'Patrimonio.Retroativo'},
                {hasAccess: false, name: 'ContaContabil.Normal'}
            ]
            wrapper = shallowMount(Manualize, {
                localVue,
                store
            })

            expect(wrapper.vm.manualizeURL).toEqual('https://manualize.com.br/categoria_patrimonio_intangivel/administrador/')
        })

        it('deve retornar url consultor quando o usuário não tiver as permissões patrimônio normal, retroativo e conta contabil normal ', () => {
            state.loki.user.authorities = [
                {hasAccess: false, name: 'Patrimonio.Normal'},
                {hasAccess: false, name: 'Patrimonio.Retroativo'},
                {hasAccess: false, name: 'ContaContabil.Normal'}
            ]
            wrapper = shallowMount(Manualize, {
                localVue,
                store
            })

            expect(wrapper.vm.manualizeURL).toEqual('https://manualize.com.br/categoria_patrimonio_intangivel/consultor/')
        })
    })
})

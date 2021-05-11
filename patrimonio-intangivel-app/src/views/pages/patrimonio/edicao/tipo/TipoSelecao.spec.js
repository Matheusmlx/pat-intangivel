import {shallowMount} from '@vue/test-utils'
import TipoSelecao from './TipoSelecao'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'

describe('TipoSelecao', async () => {
    let wrapper, localVue, store, state

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

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
        }

        store: new Vuex.Store({state})
    })

    describe('Methods', () => {
        it('Deve chamar o metodo ', async () => {
            wrapper = shallowMount(TipoSelecao, {
                localVue,
                store: new Vuex.Store({state})
            })

            const bemIntangivel = {nome: 'SOFTWARE'}
            wrapper.vm.selecionarBem(bemIntangivel)
            await flushPromises()

            expect(wrapper.emitted().setarPatrimonio).toBeTruthy()
        })
    })
})

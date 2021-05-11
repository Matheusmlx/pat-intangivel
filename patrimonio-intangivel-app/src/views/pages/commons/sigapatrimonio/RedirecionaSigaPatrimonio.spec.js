import { shallowMount } from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import { actionTypes } from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import RedirecionaSigaPatrimonio from './RedirecionaSigaPatrimonio'

describe('RedirecionaSigaPatrimonio', () => {
    let actions, store, localVue

    const linkSiga = 'http://teste'

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        actions = {
            [actionTypes.COMUM.BUSCAR_LINK_SIGAPATRIMONIO]: jest.fn().mockReturnValue(linkSiga)
        }

        store = new Vuex.Store({ actions })
    })

    describe('@created', () => {
        it('deve renderizar somente a barra de ações sem dados e sem ações', async () => {
            window.location.assign = jest.fn()

            shallowMount(RedirecionaSigaPatrimonio, {
                store,
                localVue
            })

            await flushPromises()
            expect(actions[actionTypes.COMUM.BUSCAR_LINK_SIGAPATRIMONIO]).toHaveBeenCalled()
            expect(window.location.assign).toHaveBeenCalledWith(linkSiga)
        })
    })
})

import {shallowMount} from '@vue/test-utils'
import ProximasLicencasAVencer from '@/views/pages/dashboard/vencimentolicencas/ProximasLicencasAVencer'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('ProximasLicencasAVencer', () => {

    let localVue, wrapper

    beforeEach(() => {
        localVue = applicationTestBuilder.build()


    })

    describe('methods', () => {

        it('Deve emitir o evento de atualizar para o compoente pai', async () => {
            wrapper = shallowMount(ProximasLicencasAVencer, {
                localVue
            })

            let licenca = {
                id:1
            }

            wrapper.vm.acessarLicenca(licenca.id)
            await flushPromises()

            expect(wrapper.emitted().acessarLicenca).toBeTruthy()
            expect(wrapper.emitted().acessarLicenca[0]).toEqual([1])
        })
    })
})

import {mount} from '@vue/test-utils'
import AzTextViewEditAutoCompleteFornecedor from "@/views/components/AzTextViewEditAutoCompleteFornecedor"
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from "flush-promises"
import Vuex from 'vuex'

describe('AzTextViewEditAutoCompleteFornecedor', () => {
    let wrapper, localVue, state, $validator, errors, vuetify

    localVue = applicationTestBuilder.build()
    vuetify = applicationTestBuilder.getVuetify()
    $validator = {
        _base: {
            validateAll: jest.fn().mockReturnValue(true),
            errors: {
                clear: jest.fn()
            }
        }
    }

    errors = {
        collect: jest.fn()
    }

    beforeEach(() => {
        state = {
            model: undefined,
            editing: false,
            searchValue: undefined,
            itemText: '-',
            salvo: false
        }

    })

    const fornecedores = {
        items: [
            {id: 0, cpfCnpj: '46456456', nome: 'cnpj 0'},
            {id: 0, cpfCnpj: '46456456', nome: 'cnpj 0'}
        ]
    }

    const defaultMount = () => mount(AzTextViewEditAutoCompleteFornecedor, {
        localVue,
        vuetify,
        store: new Vuex.Store({state}),
        mocks: {errors, $validator},
        propsData: {fornecedores}
    })

    describe('Methods', () => {
        it('Deve setar para true o state editing', async () => {
            wrapper = defaultMount()

            wrapper.vm.startEdit()
            await flushPromises()

            expect(wrapper.vm.editing).toEqual(true)
        })

        it('Deve enviar um emited caso o formulario esteja valido', async () => {
            wrapper = defaultMount()

            wrapper.vm.submitEdit()
            wrapper.vm.model = 'Google'
            await flushPromises()

            expect(wrapper.vm.editing).toEqual(false)
            expect(wrapper.vm.salvo).toEqual(true)
            expect(wrapper.emitted('input', 'Google')).toBeTruthy()
            expect(wrapper.emitted('input', [0])).toEqual([["Google"]])
        })

        it('Deve cancelar a edição do componente', async () => {
            wrapper = defaultMount()

            wrapper.vm.cancelEdit()
            await flushPromises()

            expect(wrapper.vm.editing).toEqual(false)
            expect(wrapper.vm.salvo).toEqual(false)
            expect(wrapper.vm.model).toEqual(wrapper.vm.value)
        })

        it('extractItemText', async () => {
            wrapper = defaultMount()

            const extractItemText = jest.fn().mockReturnValue('-')

            await flushPromises()
            extractItemText()
            await flushPromises()

            expect(extractItemText).toHaveReturnedWith('-')
        })

    })
})

import {mount} from '@vue/test-utils'
import AzTextViewEditMonth from './AzTextViewEditMonth'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('AzTextViewEditMonth', () => {
    let wrapper, localVue, $validator, errors

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        errors = {
            collect: jest.fn()
        }
        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                },
            }
        }

    })

    describe('Methods', () => {
        it('Deve emitir um evento de estar adicionando um valor', async () => {
            wrapper = mount(AzTextViewEditMonth, {
                localVue,
                mocks: {
                    $validator,
                    errors
                }
            })
            wrapper.vm.submitEdit()
            await flushPromises()

            expect(wrapper.emitted().input).toBeTruthy()
        })
        it('Deve emitir um evento quando estiver retirando a edição', async () => {
            wrapper = mount(AzTextViewEditMonth, {
                localVue,
                mocks: {
                    $validator,
                    errors
                }
            })
            wrapper.vm.openMenuDate()
            await flushPromises()
        })
        it('Deve emitir formatar a data', async () => {
            wrapper = mount(AzTextViewEditMonth, {
                localVue,
                mocks: {
                    $validator,
                    errors
                }
            })
            wrapper.vm.formatDate('11/10/2020')
            await flushPromises()
        })
    })
})

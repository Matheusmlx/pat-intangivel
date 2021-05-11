import patrimonioApiClient from './PatrimonioApiClient'

let url, config, mockRetornoApi, verboHttp, documento={idPatrimonio:1,id:2}, patrimonioId=1

jest.mock('axios', ()=>({
    get(_url){
        return new Promise(resolve =>{
            verboHttp = 'get'
            url = _url
            resolve({data:mockRetornoApi})
        })
    },
    post(_url){
        return new Promise(resolve =>{
            verboHttp = 'post'
            url = _url
            resolve({data:mockRetornoApi})
        })
    },
    delete(_url){
        return new Promise(resolve =>{
            verboHttp = 'delete'
            url = _url
            resolve({data:mockRetornoApi})
        })
    },
    put(_url){
        return new Promise(resolve =>{
            verboHttp = 'put'
            url = _url
            resolve({data:mockRetornoApi})
        })
    },
    patch(_url){
        return new Promise(resolve =>{
            verboHttp = 'patch'
            url = _url
            resolve({data:mockRetornoApi})
        })
    }
}))

describe('PatrimonioApiClient',()=>{
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('cadastrarDocumento',async()=>{
        const {data} = await patrimonioApiClient.cadastrarDocumento(documento)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual(`api/patrimonios/${documento.idPatrimonio}/documentos`)
    })
    it('atualizarDocumento',async()=>{
        const {data} = await patrimonioApiClient.atualizarDocumento(documento)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/patrimonios/${documento.idPatrimonio}/documentos/${documento.id}`)
    })
    it('uploadAnexoDocumento',async()=>{
        const formData = {}
        const {data} = await patrimonioApiClient.uploadAnexoDocumento(formData)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual('api/v1/arquivos')
    })
    it('downloadAnexo',async()=>{
        const anexo={}
        const {data} = await patrimonioApiClient.downloadAnexo(anexo)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/v1/arquivos')

    })

    it('buscarTipoDocumento',async()=>{
        const {data} = await patrimonioApiClient.buscarTipoDocumento()
        
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/tiposdocumentos?page=1&size=12')
    })

    it('deletarDocumento',async()=>{
        const documento = {id:1}
        const {data} = await patrimonioApiClient.deletarDocumento(documento)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('delete')
        expect(url).toEqual('api/patrimonios/undefined/documentos/1')
    })

    it('buscarTodos',async()=>{
        const paginacao = {
            page: 1,
            rowsPerPage: 100,
            sortDesc:[]
        }
        const filtros = {
            conteudo: {
               
            }
        }
        const {data} = await patrimonioApiClient.buscarTodos(filtros,paginacao)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/patrimonios?page=1&size=100')
    })

    it('buscarPorId',async()=>{
        const id = 1

        const {data} = await patrimonioApiClient.buscarPorId(id)
        
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/patrimonios/${id}`)

    })

    it('visualizarPorId',async()=>{
        const id = 1
        const {data} = await patrimonioApiClient.visualizarPorId(id)
        
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/patrimonios/${id}/visualizacao-completa`)
    })

    it('atualizar',async()=>{
        const dados = {}
        const {data} = await patrimonioApiClient.atualizar(dados)

         expect(data).toBeTruthy()
         expect(verboHttp).toEqual('put')
         expect(url).toEqual(`api/patrimonios/undefined`) 
    })

    it('cadastrar',async()=>{
        const dados = {}
        const {data} = await patrimonioApiClient.cadastrar(dados)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual(`api/patrimonios`,dados) 
    
    })

    it('deletar',async()=>{
        const id =  1
        const {data} = await patrimonioApiClient.deletar(id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('delete')
        expect(url).toEqual(`api/patrimonios/${id}`) 
    })

    it('deletarNaoAlterado',async()=>{
        const id = 1
        const {data} = await patrimonioApiClient.deletarNaoAlterado(id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('delete')
        expect(url).toEqual(`api/patrimonios/${id}/nao-alterado`)
    })

    it('reverterAtivacao',async()=>{
        const id = 1
        const {data} = await patrimonioApiClient.reverterAtivacao(id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('patch')
        expect(url).toEqual(`api/patrimonios/${id}/desativar`)
    })

    it('ativar',async()=>{
        const id = 1
        const {data} = await patrimonioApiClient.ativar(id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('patch')
        expect(url).toEqual(`api/patrimonios/${id}`)
    })

    it('buscarAmortizacoes',async()=>{
        const id = 1
        const {data} = await patrimonioApiClient.buscarAmortizacoes(id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/amortizacoes/${id}`)
    })

})
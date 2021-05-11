export default {
    i18n: {
        DADOS_DE_ENTRADA_EDICAO: {
            campos: {
                nome: {
                    nome: 'Nome Intangível',
                    obrigatorio: true
                },
                orgao: {
                    nome: 'Orgão'
                },
                setor: {
                    nome: 'Setor',
                    tooltip: 'Este campo contém o equivalente a Unidades Organizacionais e Setores. O campo será ativado' +
                        ' somente quando o usuário escolher um órgão'
                },
                descricao: {
                    nome: 'Descrição',
                    obrigatorio: false
                },
                estado: {
                    nome: 'Situação',
                    tooltip: 'Este campo informa se o bem ainda está em processo de desenvolvimento. Intangiveis em ' +
                        'desenvolvimento não podem ser colocados em uso, portanto não devem ser ativados e amortizados.'
                },
                valorAquisicao: {
                    nome: 'Valor De Aquisição'
                },
                reconhecimento: {
                    nome: 'Reconhecimento',
                    tooltip: 'De acordo com o MCASP, o reconhecimento define como o bem foi adquirido. ' +
                        '*A aquisição separada é oriunda de contratos de compra *Intangiveis gerados pela ' +
                        'entidade devem ser marcados como Geração Interna *Transações sem contraprestação incluem ' +
                        'doações e outras transferências.'
                },
                dataAquisicao: {
                    nome: 'Data de Aquisição',
                    obrigatorio: false
                },
                dataAtivacao: {
                    nome: 'Data de Ativação'
                },
                dataNL: {
                    nome: 'Data da NL',
                    obrigatorio: true
                },
                numeroNL: {
                    nome: 'Número da NL',
                    obrigatorio: true
                },
                fornecedor: {
                    nome: 'Fornecedor',
                    obrigatorio: false
                },
                dataVencimento: {
                    nome: 'Vencimento da Licença',
                    tooltip: 'A situação deste campo é determinada pelo tipo de reconhecimento. Aquisições separadas têm' +
                        'contrato, portanto tem a informação de vencimento da licença. Os demais tipos têm vida útil ' +
                        'determinada pelo Estado de 36 meses.'
                },
                contaContabil: {
                    nome: 'Conta Contábil'
                }
            }
        },
        DOCUMENTOS: {
            campos: {
                numero: {
                    nome: 'Número'
                },
                data: {
                    nome: 'Data',
                    obrigatorio: false
                },
                valor: {
                    nome: 'Valor',
                    obrigatorio: false
                },
                tipoDocumento: {
                    nome: 'Tipo'
                },
                uriAnexo: {
                    nome: 'Anexo',
                    obrigatorio: true
                }
            }
        },
        VISUALIZACAO: {
            campos: {
                numeroPatrimonio: {
                    nome: 'N° Patrimônio'
                },
                tipo: {
                    nome: 'Tipo'
                },
                metodo: {
                    nome: 'Método'
                },
                taxaAmortizacao: {
                    nome: 'Taxa de Amortização',
                    tooltip: 'Taxa de amortização é calculada contando os meses parciais de amortização do bem. ' +
                        'Se um patrimônio foi ativado na metade do mês, o patrimônio irá amortizar parcialmente no ' +
                        'primeiro e no último mês, mesmo que a vida útil do patrimônio seja um valor cheio.'
                },
                valorAmortizadoMensal: {
                    nome: 'Valor Amortizado Mensal'
                },
                valorAmortizadoAcumulado: {
                    nome: 'Valor Amortizado Acumulado'
                },
                valorLiquido: {
                    nome: 'Valor Líquido / Ajustado'
                },
                mesAno: {
                    nome: 'Mês/ano'
                },
                taxa: {
                    nome: 'Taxa'
                },
                valorAnteriorAmortizacao: {
                    nome: 'Valor anterior a amortização'
                },
                valorAposAmortizacao: {
                    nome: 'Valor após a amortização'
                },
                valorAmortizado: {
                    nome: 'Valor amortizado'
                },
                periodoLicenca: {
                    nome: 'Período da Licença'
                }
            }
        },
        INVENTARIO: {
            campos: {
                orgaoResponsavel: {
                    nome: 'Órgão Responsável'
                },
                mesAnoReferencia: {
                    nome: 'Mês/Ano de Referência'
                },
                tipo: {
                    nome: 'Tipo'
                }
            }
        },
        CONTAS_CONTABEIS: {
            campos: {
                codigoContabil: {
                    nome: 'Código Contábil'
                },
                contaContabil: {
                    nome: 'Conta Contábil'
                },
                tipoDaConta: {
                    nome: 'Tipo da Conta'
                },
                metoDeCalculo: {
                    nome: 'Método de Cálculo'
                }
            }
        },
        AMORTIZACAO_MANUAL: {
            campos: {
                orgaoResponsavel: {
                    nome: 'Órgão Responsável'
                },
                mesAnoDeReferencia: {
                    nome: 'Mês/Ano de Referência'
                }
            }
        },
        MOVIMENTACAO: {
            campos: {
                orgaoDestino: {
                    nome: 'Órgão Destino'
                },
                motivoObs: {
                    nome: 'Motivo/Obs.'
                },
                tooltip: {
                    tooltip: 'Finalize a última movimentação em elaboração ou exclua ela para criar nova movimentação'
                },
                dataNL: {
                    nome: 'Data NL',
                    obrigatorio: true
                }
            }
        }
    }
}

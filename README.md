# DS - PR2

## Author: Marcelino Álvarez García
## Github: https://github.com/marce100/22-23-2-PR-PR2Statement

Delivery Notes and Problems encountered:

The **recommendations** method checked in UniversityEventsPR2Test does not return data in an ordered form. Expected result:

 * idEntity10: {idEntity3, idEntity2, idEntity1}
 * idEntity7: {idEntity1, idEntity3}
 
But the result obtained is:

 * idEntity10: {idEntity2, idEntity3, idEntity1} 
 * idEntity7: {idEntity1, idEntity3}
